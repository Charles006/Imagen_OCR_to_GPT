package com.xbite.charles_prado.ui

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.mlkit.nl.entityextraction.*
import com.xbite.charles_prado.R
import com.xbite.charles_prado.databinding.FragmentInfoDisplayBinding
import com.xbite.charles_prado.gpt.ChatGPTApiClient
import kotlinx.coroutines.*
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class InfoDisplayFragment : Fragment(R.layout.fragment_info_display) {


    private lateinit var entityExtractor: EntityExtractor
    lateinit var binding: FragmentInfoDisplayBinding
    private val infoDisplayFragmentArgs: InfoDisplayFragmentArgs by navArgs()

    private lateinit var chatGPTApiClient: ChatGPTApiClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentInfoDisplayBinding.bind(view)

        val rawText : String = infoDisplayFragmentArgs.text
        //binding.rawtext.append(rawText)
        binding.textoOCR.setText(rawText)

        val maxTokens : Int = binding.txtMaxTokens.text.toString().toInt()
        val temperature : Double = binding.txtTemperature.text.toString().toDouble()
        val rawtext2 : String = binding.textoOCR.text.toString()

        chatGPTApiClient = ChatGPTApiClient(  getString(R.string.pointing_message)
        ,maxTokens,temperature)

        /*
        binding.btnGPT.setOnClickListener {
            SendMessageTask().execute(rawtext2)
        }
        */
        binding.btnGPT.setOnClickListener {
            val rawtext2: String = binding.textoOCR.text.toString()
            binding.loadingOverlay.visibility = View.VISIBLE // Muestra el FrameLayout con ProgressBar
            CoroutineScope(Dispatchers.Main).launch {
                val response = sendMessage(rawtext2)
                binding.textoRespuesta.setText(response)
                binding.loadingOverlay.visibility = View.GONE // Oculta el FrameLayout con ProgressBar
            }
        }
    }
    private suspend fun sendMessage(message: String): String = withContext(Dispatchers.Default) {
        return@withContext suspendCancellableCoroutine { continuation ->
            chatGPTApiClient.sendMessage_to_gpt35(message) { result ->
                continuation.resume(result)
            }
        }
    }


    private inner class SendMessageTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String): String {
            val message = params[0] ?: ""
            var response = ""

            chatGPTApiClient.sendMessage_to_gpt35(message) { result ->
                response = result
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            binding.textoRespuesta.setText(result)
            binding.progressBar.visibility = View.GONE
        }
    }
}