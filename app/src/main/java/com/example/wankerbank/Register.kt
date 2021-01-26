package com.example.wankerbank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_dashboard.view.*
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.Exception

class Register : AppCompatActivity() {
    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun validateName () : Boolean  {
        val value = name.editText?.text.toString()

        return if (value.isEmpty()) {
            name.error = "Field cannot be empty"
            name.isErrorEnabled = true
            false
        } else {
            name.error = null
            name.isErrorEnabled = false
            true
        }
    }
    private fun validatePhone () : Boolean  {
        val value = phone.editText?.text.toString()

        return if (value.isEmpty()) {
            phone.error = "Field cannot be empty"
            phone.isErrorEnabled = true
            false
        } else {
            phone.error = null
            phone.isErrorEnabled = false
            true
        }
    }
    private fun validateEmail () : Boolean  {
        val value = email.editText?.text.toString()

        return if (value.isEmpty()) {
            email.error = "Field cannot be empty"
            email.isErrorEnabled = true
            false
        } else {
            email.error = null
            email.isErrorEnabled = false
            if (!isEmailValid(value)) {
                email.error = "Invalid Email"
            }
            true
        }
    }
    private fun validateAddress () : Boolean  {
        val value = address.editText?.text.toString()

        return if (value.isEmpty()) {
            address.error = "Field cannot be empty"
            address.isErrorEnabled = true
            false
        } else {
            address.error = null
            address.isErrorEnabled = false
            true
        }
    }
    private fun validateIdentityCard () : Boolean  {
        val value = identityCard.editText?.text.toString()

        return if (value.isEmpty()) {
            identityCard.error = "Field cannot be empty"
            identityCard.isErrorEnabled = true
            false
        } else {
            identityCard.error = null
            identityCard.isErrorEnabled = false
            true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_register)
        print("Hello World 0099999999999999999999999999999999")
        confirm.setOnClickListener() { confirmOnClick() }

        backLogin.setOnClickListener {
            val intent = Intent(this@Register, Login::class.java)

            startActivity(intent)

            finish()
        }
    }

    private fun confirmOnClick()  {
        if (!validateName() or !validatePhone() or !validateEmail() or !validateAddress() or !validateIdentityCard()) {
            return
        }

    }
}