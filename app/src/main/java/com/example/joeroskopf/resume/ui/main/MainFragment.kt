package com.example.joeroskopf.resume.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import com.example.joeroskopf.resume.CommonHelloService
import com.example.joeroskopf.resume.R
import com.example.joeroskopf.resume.network.TacoService
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.coroutines.experimental.async
import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    lateinit var commonHelloService: CommonHelloService

    @Inject lateinit var tacoService: TacoService

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.main_fragment, container, false)

        view.findViewById<Button>(R.id.button).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_detailFragment)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
        message.text = commonHelloService.sayHello()


        async {
            tacoService.getRandomRaco().subscribe({
                Log.d("D","tacoDebug - all good! ${it.shell}")
            }, {
                Log.d("D","tacoDebug - all bad ${it.localizedMessage}")
            })
        }
    }

}
