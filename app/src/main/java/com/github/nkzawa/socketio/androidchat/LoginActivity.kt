package com.github.nkzawa.socketio.androidchat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView.OnEditorActionListener
import com.github.nkzawa.socketio.androidchat.Constants.ADD_USER
import com.github.nkzawa.socketio.androidchat.Constants.LOGIN
import com.github.nkzawa.socketio.androidchat.Constants.NUMBER_OF_USER
import com.github.nkzawa.socketio.androidchat.Constants.USERNAME
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject

/**
 * A login screen that offers login via username.
 */
class LoginActivity : Activity() {
    //    private var mUsernameView: EditText? = null
    private var mUsername: String? = null
    private lateinit var mSocket: Socket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val app = application as ChatApplication
        mSocket = app.socket
        // Set up the login form.

        username_input.setOnEditorActionListener(OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })
        val signInButton = findViewById<View>(R.id.sign_in_button) as Button
        signInButton.setOnClickListener { attemptLogin() }
        mSocket.on(LOGIN, onLogin)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket.off(LOGIN, onLogin)
    }

    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    private fun attemptLogin() {
        // Reset errors.
        username_input.error = null

        // Store values at the time of the login attempt.
        val username = username_input.text.toString().trim { it <= ' ' }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            // There was an error; don't attempt login and focus the first
// form field with an error.
            username_input.error = getString(R.string.error_field_required)
            username_input.requestFocus()
            return
        }

        mUsername = username

        // perform the user login attempt.
        mSocket.emit(ADD_USER, username)
    }

    private val onLogin = Emitter.Listener { args ->
        args.forEachIndexed { i, it ->
            Log.e("data all $i:", it.toString())

            // result example :
            //      {
            //          "numUsers":25
            //      }
        }

        val data = args[0] as JSONObject
        val numUsers: Int
        numUsers = try {
            data.getInt(NUMBER_OF_USER)
        } catch (e: JSONException) {
            return@Listener
        }
        setResult(RESULT_OK, Intent().apply {
            putExtra(USERNAME, mUsername)
            putExtra(NUMBER_OF_USER, numUsers)
        })
        finish()
    }
}