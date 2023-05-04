package com.xbite.charles_prado.gpt

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ChatGPTApiClient(
    private val apiKey: String ,
    private val max_tokens: Int,
    private val temperature: Double

) {

    fun sendMessage_to_davinci(message: String, callback: (String) -> Unit) {
        try {
            //val url = URL("https://api.openai.com/v1/engines/davinci-codex/completions")
            val url = URL("https://api.openai.com/v1/completions")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "POST"
            httpURLConnection.setRequestProperty("Authorization", "Bearer $apiKey")
            httpURLConnection.setRequestProperty("Content-Type", "application/json")
            httpURLConnection.doOutput = true
            val request = JSONObject()
            request.put("model", "text-davinci-003")
            request.put("prompt", message)
            request.put("max_tokens", max_tokens)
            request.put("temperature", temperature)
            val outputStream = httpURLConnection.outputStream
            outputStream.write(request.toString().toByteArray())
            outputStream.flush()
            outputStream.close()
            val responseCode = httpURLConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val bufferedReader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
                val stringBuilder = StringBuilder()
                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }
                bufferedReader.close()
                val response = JSONObject(stringBuilder.toString())
                val message = response.getJSONArray("choices").getJSONObject(0).getString("text")
                callback(message)
            } else {
                callback("")
            }

        }
        catch (ex: Exception){
            throw ex

        }

    }
    fun sendMessage_to_gpt35(message: String, callback: (String) -> Unit) {
        try {
            val url = URL("https://api.openai.com/v1/chat/completions")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "POST"
            httpURLConnection.setRequestProperty("Authorization", "Bearer $apiKey")
            httpURLConnection.setRequestProperty("Content-Type", "application/json")
            httpURLConnection.doOutput = true


            val json ="{\n" +
                    "\"model\": \"gpt-3.5-turbo\", \n" +
                    "\"messages\": \n" +
                    "[\n" +
                    "    {\n" +
                    "        \"role\":\"user\",\n" +
                    "        \"content\":\""+ message + "\"    \n" +
                    "        }\n" +
                    "]\n" +
                    "\n" +
                    "}"
            val outputStream = httpURLConnection.outputStream
            outputStream.write(json.toString().toByteArray())
            outputStream.flush()
            outputStream.close()
            val responseCode = httpURLConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val bufferedReader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
                val stringBuilder = StringBuilder()
                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }
                bufferedReader.close()
                val response = JSONObject(stringBuilder.toString())
                val message = response.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content")
                callback(message)
            } else {
                callback(responseCode.toString())
            }

        }
        catch (ex: Exception){
            throw ex

        }

    }

}
