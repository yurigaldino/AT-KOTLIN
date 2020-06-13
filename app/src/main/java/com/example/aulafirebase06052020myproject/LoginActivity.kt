package com.example.aulafirebase06052020myproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var callbackManager = CallbackManager.Factory.create()
    private lateinit var auth: FirebaseAuth
    private val TAG = "FirebaseEmailPassword"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        btn_dummy_login.setOnClickListener {
//            val intent = Intent(this, CadastroAtualizacaoActivity::class.java)
//            startActivity(intent)
//        }

        btn_email_sign_in.setOnClickListener {
            signIn(edtEmail.text.toString(), edtPassword.text.toString())
        }
        btn_email_create_account.setOnClickListener{
            createAccount(edtEmail.text.toString(), edtPassword.text.toString())
        }

        auth = FirebaseAuth.getInstance()

        buttonFacebookLogin.setReadPermissions("email", "public_profile")
        buttonFacebookLogin.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
//                Log.d(TAG, "facebook:onSuccess:$loginResult")
                Toast.makeText(applicationContext, "${loginResult}", Toast.LENGTH_SHORT).show()
                handleFacebookAccessToken(loginResult.accessToken)
            }
            override fun onCancel() {
                Toast.makeText(applicationContext, "Você precisa estar logado para usar o app.", Toast.LENGTH_SHORT).show()
//                Log.d(TAG, "facebook:onCancel")
                // ...
            }
            override fun onError(error: FacebookException) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
//                Log.d(TAG, "facebook:onError", error)
                // ...
            }
        })
        //conferir se ja esta logado facebook
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null){
            val intent = Intent(this, CadastroAtualizacaoActivity::class.java)
            startActivity(intent)
        }

    }


    private fun createAccount(email: String, password: String) {
        Log.e(TAG, "createAccount:" + email)
        if (!validateForm(email, password)) {
            return
        }

        auth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "Conta criada com sucesso.")
                    Toast.makeText(applicationContext, "Conta criada com sucesso.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Email já em uso ou senha inválida.", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun signIn(email: String, password: String) {
        Log.e(TAG, "signIn:" + email)
        if (!validateForm(email, password)) {
            return
        }

        auth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.e(TAG, "signIn: Success!")
                    val intent = Intent(this, CadastroAtualizacaoActivity::class.java)
                    startActivity(intent)

                } else {
                    Log.e(TAG, "signIn: Fail!", task.exception)
                    Toast.makeText(applicationContext, "Falha em autenticar!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateForm(email: String, password: String): Boolean {

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Coloque um endereço de email", Toast.LENGTH_SHORT).show()
            return false
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Insira uma senha.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(applicationContext, "Senha curta demais, insira pelo menos 6 dígitos.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


    private fun handleFacebookAccessToken(token: AccessToken) {
//        Log.d(TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        val auth = FirebaseAuth.getInstance()
        val task = auth.signInWithCredential(credential)
            task.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    LoginManager.getInstance().logOut()
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(applicationContext, "Bem vindo, ${auth.currentUser}!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, CadastroAtualizacaoActivity::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Autenticação falhou... ${task.exception}",
                        Toast.LENGTH_SHORT).show()
                }

                // ...
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

}
