package com.example.joeroskopf.resume.ui.main

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.*
import com.example.joeroskopf.resume.R
import com.example.joeroskopf.resume.db.TacoRepository
import com.example.joeroskopf.resume.model.network.TacoResponse
import com.example.joeroskopf.resume.network.TacoService
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.browse
import ru.noties.markwon.Markwon
import ru.noties.markwon.SpannableConfiguration
import javax.inject.Inject



class MainFragment : Fragment() {

    @Inject
    lateinit var tacoService: TacoService
    @Inject
    lateinit var tacoRepository: TacoRepository

    private lateinit var viewModel: MainViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_menu_save_button -> {
                async {
                    saveTaco()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!, MainViewModelFactory(tacoService, tacoRepository)).get(MainViewModel::class.java)
        setHasOptionsMenu(true)

        hideDisplayLayout()

        fetchTaco()

        //set on click for the fab refresh button
        mainFragmentRefreshFab.setOnClickListener {
            invalidateAndRefresh()
        }
    }

    /**
     * Show the [View] elements that should be visible after the API has returned valid results
     */
    private fun showDisplayLayout() {
        mainFragmentTacoDisplayContainer.visibility = View.VISIBLE
        mainFragmentProgressBar.visibility = View.GONE
        mainFragmentErrorLayout.visibility = View.GONE
    }

    /**
     * Show the [View] elements that should be visible while we are calling the API
     */
    private fun hideDisplayLayout() {
        mainFragmentTacoDisplayContainer.visibility = View.GONE
        mainFragmentProgressBar.visibility = View.VISIBLE
        mainFragmentErrorLayout.visibility = View.GONE
    }

    /**
     * Show the [View] elements that should be visible when the API returns from an error
     */
    private fun showErrorLayout() {
        mainFragmentTacoDisplayContainer.visibility = View.GONE
        mainFragmentProgressBar.visibility = View.GONE
        mainFragmentErrorLayout.visibility = View.VISIBLE
    }

    /**
     * Fetch the taco from the api! This method is responsible for updating the UI after the response has returned
     */
    private fun fetchTaco() {
        viewModel.getTacoResponse().observe(activity as LifecycleOwner, Observer<TacoResponse> {
            if (it == null) {
                val snackbar = Snackbar.make(activity!!.findViewById(R.id.bottom_navigation),
                        R.string.taco_error, Snackbar.LENGTH_LONG)
                snackbar.setAction(R.string.retry_string, {
                    invalidateAndRefresh()
                })
                snackbar.show()
                showErrorLayout()
                Log.e("MainFragment","an error occurred while observing the taco")
            } else {
                showDisplayLayout()
            }
            displayResults(it)
        })
    }

    /**
     * Invalidate our local data and fetch a taco
     */
    private fun invalidateAndRefresh() {
        //set view state
        hideDisplayLayout()
        //invalidate local data
        viewModel.invalidateLocalData()
        //fetch taco from API
        fetchTaco()
    }

    /**
     * Given an optional [TacoResponse] from the API, display a stylized summary of the taco
     */
    private fun displayResults(tacoResponse: TacoResponse?) {
        tacoResponse?.let {
            mainFragmentHeadingText.text = MainViewModel.getHeadingFromTaco(it)
            val configuration = SpannableConfiguration.builder(context!!)
                    .linkResolver { _, link ->
                        context!!.browse(link)
                    }
                    .build()

            Markwon.setMarkdown(mainFragmentBaseLayerText, configuration, it.baseLayer?.recipe ?: "")
            mainFragmentBaseLayerText.movementMethod = LinkMovementMethod.getInstance()

            Markwon.setMarkdown(mainFragmentMixinText, configuration, it.mixin?.recipe ?: "")
            mainFragmentMixinText.movementMethod = LinkMovementMethod.getInstance()

            Markwon.setMarkdown(mainFragmentCondimentText, configuration, it.condiment?.recipe ?: "")
            mainFragmentCondimentText.movementMethod = LinkMovementMethod.getInstance()

            Markwon.setMarkdown(mainFragmentSeasoningText, configuration, it.seasoning?.recipe ?: "")
            mainFragmentSeasoningText.movementMethod = LinkMovementMethod.getInstance()

            Markwon.setMarkdown(mainFragmentShellText, configuration, it.shell?.recipe ?: "")
            mainFragmentShellText.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    @SuppressLint("CheckResult")
    private suspend fun saveTaco() {
        viewModel.saveTacoLocally().subscribe({
            this.tacoSavedSuccessfully(it)
        }, {
            this.tacoSaveOnError(it)
        })
    }

    /**
     * If the Saving / Deleting of a taco did go well, alert the user accordingly based on
     * @param saved - if the taco was saved or deleted
     */
    private fun tacoSavedSuccessfully(saved: Boolean) {
        if (saved) {
            Snackbar.make(activity!!.findViewById(R.id.bottom_navigation), getString(R.string.taco_saved_success), Snackbar.LENGTH_SHORT).show()
        } else {
            Snackbar.make(activity!!.findViewById(R.id.bottom_navigation), getString(R.string.taco_removed_success), Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * If Saving / Deleting a [TacoResponse] did not go well, we will alert the user
     */
    private fun tacoSaveOnError(throwable: Throwable) {
        Snackbar.make(activity!!.findViewById(R.id.bottom_navigation), throwable.localizedMessage, Snackbar.LENGTH_SHORT).show()
    }
}
