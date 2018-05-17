package com.example.joeroskopf.resume.ui.main

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.joeroskopf.resume.model.network.TacoResponse
import com.example.joeroskopf.resume.network.TacoService
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject
import com.example.joeroskopf.resume.R
import android.support.design.widget.Snackbar
import android.view.*
import com.example.joeroskopf.resume.db.TacoRepository
import kotlinx.coroutines.experimental.async


class MainFragment : Fragment() {

    @Inject
    lateinit var tacoService: TacoService
    @Inject
    lateinit var tacoRepository: TacoRepository

    private lateinit var viewModel: MainViewModel

    companion object {
        fun newInstance() = MainFragment()
    }

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

    @SuppressLint("CheckResult")
    private suspend fun saveTaco() {
        viewModel.saveTacoLocally().subscribe({
            this.tacoSavedSuccessfully(it)
        }, {
            this.tacoSaveOnError(it)
        })
    }

    private fun tacoSavedSuccessfully(success: Boolean) {
        if (success) {
            Snackbar.make(mainFragmentBaseLayout, "Taco saved successfully!", Snackbar.LENGTH_SHORT).show()
        } else {
            Snackbar.make(mainFragmentBaseLayout, "Removed taco!", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun tacoSaveOnError(throwable: Throwable) {
        Snackbar.make(mainFragmentBaseLayout, throwable.localizedMessage, Snackbar.LENGTH_SHORT).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.main_fragment, container, false)

/*        view.findViewById<Button>(R.id.button).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_detailFragment)
        }*/

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!, MainViewModelFactory(tacoService, tacoRepository)).get(MainViewModel::class.java)
        setHasOptionsMenu(true)

        hideDisplayLayout()

        fetchTaco()

        mainFragmentRefreshFab.setOnClickListener {
            hideDisplayLayout()
            viewModel.invalidateLocalData()
            fetchTaco()
        }
    }

    /**
     * Show the [View] elements that should be visible after the API has returned valid results
     */
    private fun showDisplayLayout() {
        mainFragmentTacoDisplayContainer.visibility = View.VISIBLE
        mainFragmentProgressBar.visibility = View.GONE
    }

    /**
     * Show the [View] elements that should be visible while we are calling the API
     */
    private fun hideDisplayLayout() {
        mainFragmentTacoDisplayContainer.visibility = View.GONE
        mainFragmentProgressBar.visibility = View.VISIBLE
    }

    /**
     * Fetch the taco from the api! This method is responsible for updating the UI after the response has returned
     */
    private fun fetchTaco() {
        viewModel.getTacoResponse().observe(activity as LifecycleOwner, Observer<TacoResponse> {
            if (it == null) {
                //TODO display error snackbar
            } else {
                showDisplayLayout()
            }
            displayResults(it)
        })
    }

    /**
     * Given an optional [TacoResponse] from the API, display a stylized summary of the taco
     */
    private fun displayResults(tacoResponse: TacoResponse?) {
        tacoResponse?.let {
            mainFragmentHeadingText.text = viewModel.getHeadingFromTaco(it)
            mainFragmentBaseLayerText.loadMarkdown(it.baseLayer?.recipe)
            mainFragmentMixinText.loadMarkdown(it.mixin?.recipe)
            mainFragmentCondimentText.loadMarkdown(it.condiment?.recipe)
            mainFragmentSeasoningText.loadMarkdown(it.seasoning?.recipe)
            mainFragmentShellText.loadMarkdown(it.shell?.recipe)
        }
    }
}
