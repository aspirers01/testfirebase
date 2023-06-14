package com.example.testfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth= FirebaseAuth.getInstance()

        checkLoggedInState()
       findViewById<Button>(R.id.btsignout).setOnClickListener{

           auth.signOut();
           checkLoggedInState()
       }

        findViewById<Button>(R.id.btnRegister).setOnClickListener{
                registerUser()
        }
        findViewById<Button>(R.id.btnLogin).setOnClickListener{
            loginUser()
        }

    }




    private fun registerUser(){
       var email= findViewById<EditText>(R.id.etEmailRegister).text.toString()
        var password=findViewById<EditText>(R.id.etPasswordRegister).text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main){
                        checkLoggedInState()
                    }
                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity,e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun loginUser(){
        var email= findViewById<EditText>(R.id.etEmailLogin).text.toString()
        var password=findViewById<EditText>(R.id.etPasswordLogin).text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main){
                        checkLoggedInState()
                    }
                }catch (e:Exception){
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@MainActivity,e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


    private fun checkLoggedInState(){
        if(auth.currentUser==null){
            findViewById<TextView>(R.id.tvLoggedIn).text="you are not logged in"
        }else{
            findViewById<TextView>(R.id.tvLoggedIn).text="you are logged in"
        }
    }
}