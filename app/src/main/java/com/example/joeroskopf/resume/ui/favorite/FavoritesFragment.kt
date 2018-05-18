package com.example.joeroskopf.resume.ui.favorite

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.example.joeroskopf.resume.R
import com.example.joeroskopf.resume.db.TacoEntity
import com.example.joeroskopf.resume.db.TacoRepository
import com.example.joeroskopf.resume.model.network.TacoResponse
import com.example.joeroskopf.resume.ui.detail.DetailActivity
import com.example.joeroskopf.resume.ui.favorite.adapters.FavoritesAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.favorites_fragment.*
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.bundleOf
import javax.inject.Inject

class FavoritesFragment : Fragment() {

    @Inject
    lateinit var tacoRepository: TacoRepository

    private lateinit var viewModel: FavoritesViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.favorites_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!, FavoriteViewModelFactory(tacoRepository)).get(FavoritesViewModel::class.java)

        viewModel.fetchAllTacos().observe(activity as LifecycleOwner, Observer<List<TacoEntity>> {
            if (it?.size ?: 0 > 0) {
                showLayout()
                setupList(it)
            } else {
                hideLayout()
            }
        })
    }

    /**
     * Show the RV layout when we have valid tacos to display
     */
    private fun showLayout() {
        if (include_favorites != null && favoritesFragmentRecyclerView != null) {
            include_favorites.visibility = View.GONE
            favoritesFragmentRecyclerView.visibility = View.VISIBLE
        }
    }

    /**
     * Hide the RV layout when we have nothing to display. Instead showing a message to the user
     */
    private fun hideLayout() {
        if (include_favorites != null && favoritesFragmentRecyclerView != null) {
            include_favorites.visibility = View.VISIBLE
            favoritesFragmentRecyclerView.visibility = View.GONE
        }
    }

    /**
     * Display out items in the recyclerview
     */
    private fun setupList(tacos: List<TacoEntity>?) {
        tacos?.let {
            //our fragment probably hasn't been inflated yet if the RV is null
            if (favoritesFragmentRecyclerView != null) {
                // Creates a vertical Layout Manager
                favoritesFragmentRecyclerView.layoutManager = LinearLayoutManager(context)

                // Access the RecyclerView Adapter and load the data into it
                favoritesFragmentRecyclerView.adapter = FavoritesAdapter(
                        object : FavoritesAdapter.OnItemClickListener {
                            override fun onItemClick(tacoResponse: TacoResponse) {
                                val bundle = bundleOf(DetailActivity.ARGUMENT_DETAIL_FRAGMENT_TACO to tacoResponse)
                                Navigation.findNavController(activity!!, R.id.nav_host_fragment).navigate(R.id.action_favoritesFragment_to_detailActivity, bundle)
                            }

                            override fun onItemCloseClicked(tacoEntity: TacoEntity) {
                                async {
                                    tacoRepository.deleteTaco(tacoEntity)
                                }
                            }
                        },
                        it,
                        context!!
                )
            }
        }
    }
}
