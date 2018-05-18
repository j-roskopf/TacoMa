package com.example.joeroskopf.resume.ui.detail

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.MenuItem
import com.example.joeroskopf.resume.R
import com.example.joeroskopf.resume.model.network.TacoResponse
import com.example.joeroskopf.resume.ui.main.MainViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.browse
import ru.noties.markwon.Markwon
import ru.noties.markwon.SpannableConfiguration

class DetailActivity : AppCompatActivity() {

    companion object {
        const val ARGUMENT_DETAIL_FRAGMENT_TACO = "taco"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if(intent.extras.containsKey(DetailActivity.ARGUMENT_DETAIL_FRAGMENT_TACO)) {
            Log.d("D","detailDebug - it did contain the key!")

            val tacoResponse = intent.extras[DetailActivity.ARGUMENT_DETAIL_FRAGMENT_TACO] as TacoResponse
            displayResults(tacoResponse)
        } else {
            Log.d("D","detailDebug - in did not contain the key")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Given an optional [TacoResponse] from the API, display a stylized summary of the taco
     */
    private fun displayResults(tacoResponse: TacoResponse?) {
        tacoResponse?.let {
            detailActivityHeadingText.text = MainViewModel.getHeadingFromTaco(it)
            val configuration = SpannableConfiguration.builder(this)
                    .linkResolver { _, link ->
                        browse(link)
                    }
                    .build()

            Markwon.setMarkdown(detailActivityBaseLayerText, configuration, it.baseLayer?.recipe
                    ?: "")
            detailActivityBaseLayerText.movementMethod = LinkMovementMethod.getInstance()

            Markwon.setMarkdown(detailActivityMixinText, configuration, it.mixin?.recipe ?: "")
            detailActivityMixinText.movementMethod = LinkMovementMethod.getInstance()

            Markwon.setMarkdown(detailActivityCondimentText, configuration, it.condiment?.recipe
                    ?: "")
            detailActivityCondimentText.movementMethod = LinkMovementMethod.getInstance()

            Markwon.setMarkdown(detailActivitySeasoningText, configuration, it.seasoning?.recipe
                    ?: "")
            detailActivitySeasoningText.movementMethod = LinkMovementMethod.getInstance()

            Markwon.setMarkdown(detailActivityShellText, configuration, it.shell?.recipe ?: "")
            detailActivityShellText.movementMethod = LinkMovementMethod.getInstance()
        }
    }
}
