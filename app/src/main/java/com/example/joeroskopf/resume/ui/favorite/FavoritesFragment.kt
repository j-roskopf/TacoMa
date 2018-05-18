package com.example.joeroskopf.resume.ui.favorite

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.joeroskopf.resume.R
import com.example.joeroskopf.resume.db.TacoEntity
import com.example.joeroskopf.resume.db.TacoRepository
import com.example.joeroskopf.resume.model.network.TacoResponse
import com.example.joeroskopf.resume.ui.favorite.adapters.FavoritesAdapter
import com.example.joeroskopf.resume.ui.main.MainViewModel
import com.example.joeroskopf.resume.ui.main.MainViewModelFactory
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.favorites_fragment.*
import kotlinx.coroutines.experimental.async
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
            setupList(it)
        })
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
                            override fun onItemClick(tacoEntity: TacoEntity) {
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
