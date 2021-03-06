package com.hungames.cookingsocial.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.hungames.cookingsocial.LoginStatusViewModel
import com.hungames.cookingsocial.MainActivity
import com.hungames.cookingsocial.R
import com.hungames.cookingsocial.data.PreferencesManager
import com.hungames.cookingsocial.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    /**
     * for handling login UI and repository calls
     */
    @Singleton
    private val loginViewModel: LoginViewModel by viewModels()

    /**
     * for handling status of Login. This model is also used by other activities
      */
    @Singleton
    private val loginStatusViewModel: LoginStatusViewModel by viewModels()

    @Inject
    lateinit var preferenceManager: PreferencesManager

    private lateinit var loginDataBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        // Check if user is logged in from previous session
        lifecycleScope.launch { getLoginStatus() }

        val username = loginDataBinding.username
        val password = loginDataBinding.password
        val login = loginDataBinding.login
        val loading = loginDataBinding.loading
        val register = loginDataBinding.register

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid
            register.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                // TODO: add more keys and storage to save user profile and get them at appropriate time
                loginStatusViewModel.sendEventLogin()
                updateUiWithUser(loginResult.success)
            }

            //Complete and destroy login activity once successful
        })

        loginViewModel.registerResult.observe(this@LoginActivity, Observer {
            val registerResult = it ?: return@Observer
            loading.visibility = View.GONE
            if (registerResult.error != null){
                showRegisterFailed(registerResult.error)

                // do some work perhaps cleaning fields
            }
        })

        lifecycleScope.launchWhenStarted {
            loginStatusViewModel.statusEvent.collect { event ->
                when (event){
                    LoginStatusViewModel.StatusEvent.Login -> {
                        // Login
                        navigateToMainActivity()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    else -> {
                        // Logout
                    }
                }

            }
        }

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                        username.text.toString(),
                        password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                                username.text.toString(),
                                password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
            register.setOnClickListener{
                loading.visibility = View.VISIBLE
                loginViewModel.register(username.text.toString(), password.text.toString())
            }
        }
    }

    private suspend fun getLoginStatus() {
        if (preferenceManager.preferenceFlow.first().loginStatus) {
            navigateToMainActivity()
            setResult(Activity.RESULT_OK)
            finish()
        }
    }


    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        Toast.makeText(
                applicationContext,
                "$welcome $displayName",
                Toast.LENGTH_LONG
        ).show()
        navigateToMainActivity()
    }

    private fun navigateToMainActivity(){
        val navigatingIntent = Intent(this, MainActivity::class.java)
        startActivity(navigatingIntent)
    }

    private fun showRegisterFailed(@StringRes errorString: Int){
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}