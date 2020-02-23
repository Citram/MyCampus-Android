package io.github.citram.mycampus

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.loopj.android.http.RequestParams
import com.loopj.android.http.TextHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONException

/**
 * The class for the main activity. Displays the login activity.
 */
class LoginActivity : AppCompatActivity() {

    var error: String = ""

    /**
     * On create displays the activity with the login and register buttons.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Global.getLoggedUser().equals("N/A")) {
            setContentView(R.layout.activity_main)

            val loginButton = findViewById<Button>(R.id.login_button)
            loginButton.setOnClickListener {
                login()
            }

            val registerButton = findViewById<Button>(R.id.register)
            registerButton.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
                finish()
            }

            refreshErrorMessage()
        } else {
            setContentView(R.layout.activity_main_logged)
            val homeBtn = findViewById<Button>(R.id.homepage_back)
            homeBtn.setOnClickListener {
                startActivity(Intent(this@LoginActivity, HomepageActivity::class.java))
            }
        }
    }

    /**
     * Function for logging in.
     */
    private fun login() {
        error = ""
        val username = findViewById<EditText>(R.id.username).text
        val password = findViewById<EditText>(R.id.password).text
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            error = "Please complete all fields"
            refreshErrorMessage()
            return
        }
        val params = RequestParams()
        val request = StringBuilder()
        request.append("/").append(username).append("/login")
        params.put("password", password)

        HttpUtils.get(request.toString(), params, object : TextHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header?>?,
                response: String?
            ) {
                if (response.equals("false")) {
                    error = "Password is incorrect"
                } else {
                    // open up homepage of user
                    Global.setLoggedUser(username.toString())
                    startActivity(Intent(this@LoginActivity, HomepageActivity::class.java))
                    finish()
                }
                refreshErrorMessage()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                errorResponse: String?,
                throwable: Throwable?
            ) {
                errorResponse?.contains("Student does not exist")
                error += try {
                    "Student does not exist"
                } catch (e: JSONException) {
                    e.message
                }
                refreshErrorMessage()
            }
        })
    }

    /**
     * Function for refreshing error when there are errors when logging in.
     */
    private fun refreshErrorMessage() {
        val errorMsg = findViewById<TextView>(R.id.error)
        errorMsg.text = error
        if (error.isEmpty()) {
            errorMsg.visibility = View.GONE
        } else {
            errorMsg.visibility = View.VISIBLE
        }
    }
}
