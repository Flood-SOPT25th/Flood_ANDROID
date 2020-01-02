package com.flood_android.ui.login

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.c.loginflood.PostLoginRequest
import com.c.loginflood.PostLoginResponse
import com.flood_android.R
import com.flood_android.network.ApplicationController
import com.flood_android.ui.main.MainActivity
import com.flood_android.ui.post.PostActivity
import com.flood_android.util.GlobalData
import com.flood_android.util.SharedPreferenceController
import com.flood_android.util.safeEnqueue
import com.flood_android.util.toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val loginAlertDialog by lazy {
        LoginAlertDialog()
    }

    var idFlag = false
    var pwFlag = false

    lateinit var websiteUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edtLoginId: EditText = findViewById(R.id.edt_login_id)
        val edtLoginPw: EditText = findViewById(R.id.edt_login_pw)
        val tvFind: TextView = findViewById(R.id.tv_login_find_idpw)
        val tvLogin: TextView = findViewById(R.id.tv_login_login)
        val clLogin: ConstraintLayout = findViewById(R.id.cl_login_login)
        val clSignup: ConstraintLayout = findViewById(R.id.cl_login_signup)


        edtIdToBlue(edt_login_id)
        edtPostToBlue(edt_login_pw)

        // 로그인 클릭 시
        clLogin.setOnClickListener {
            var user_email = edt_login_id.text.toString()
            var user_pw: String = edt_login_pw.text.toString()
            // 유효성 검사 후 로그인 통신
            if (isValid(user_email, user_pw)) {
                postLogin(user_email, user_pw)
            }
            /*val intent = Intent(this@MainActivity, MainActivity::class.java)
            startActivity(intent)*/
        }

        connectWeb()

    }

    private fun connectWeb() {
        var intent = getIntent()
        var action: String? = intent.getAction()
        var type: String? = intent.getType()
        // 인텐트 정보가 있는 경우 실행
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                websiteUrl = intent.getStringExtra(Intent.EXTRA_TEXT)
                //edt_post_url.setText(websiteUrl)
                if (SharedPreferenceController.getAuthorization(this@LoginActivity).toString() == ""){
                    toast("로그인을 해주세요~^~^")
                }
                else {
                    val intent = Intent(this@LoginActivity, PostActivity::class.java)
                    intent.putExtra("websiteUrl", websiteUrl)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    var fail: (Throwable) -> Unit = {
        Log.v("LoginActivity", "fail")
        Log.v("LoginActivity", it.message.toString())
        Log.v("LoginActivity", it.toString())
    }
    var temp: (PostLoginResponse) -> Unit = {
        Log.v("LoginActivity", "temp")
        Log.v("LoginActivity", it.message)
        if (it.message == "로그인 완료"){
            SharedPreferenceController.clearSPC(this@LoginActivity)
            SharedPreferenceController.setAuthorization(this@LoginActivity, it.data?.token)
            Log.v("청하", it.data?.token.toString())
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        if (it.message == "비밀번호가 다릅니다."){
            loginAlertDialog.show(supportFragmentManager, "login Alert Dialog")
            GlobalData.loginDialogMessage = "비밀번호가 일치하지 않습니다"
        }
        if (it.message == "존재하지 않는 계정 입니다."){
            loginAlertDialog.show(supportFragmentManager, "login Alert Dialog")
            GlobalData.loginDialogMessage = "존재하지 않는 계정 입니다"
        }


    }
    private fun postLogin(user_email: String, user_pw: String
    ) {
        val postLoginResponse = ApplicationController.networkServiceUser
            .postLoginResponse("application/json", PostLoginRequest(user_email, user_pw))
        postLoginResponse.safeEnqueue(fail, temp)
    }


    private fun isValid(u_id: String, u_pw: String): Boolean {
        if (u_id == "") {
        } else if (u_pw == "") {
            // 비밀번호가 일치하지 않습니다
        } else return true
        return false
    }

    private fun edtIdToBlue(edt1: EditText) {
        edt1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if ((s?: "").isNotEmpty()){
                    idFlag = true
                    if(pwFlag){
                        cl_login_login.setBackgroundResource(R.drawable.rect_blue_white_24dp)
                        tv_login_login.setTextColor(Color.parseColor("#ffffff"))
                    }
                    else{
                        cl_login_login.setBackgroundResource(R.drawable.rect_white_blue_24dp)
                        tv_login_login.setTextColor(Color.parseColor("#0057ff"))
                    }
                } else {
                    // 비활성화
                    idFlag = false
                }


            }
        })
    }

    private fun edtPostToBlue(edt1: EditText) {
        edt1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if ((s?: "").isNotEmpty()){
                    pwFlag = true
                    if(idFlag){
                        cl_login_login.setBackgroundResource(R.drawable.rect_blue_white_24dp)
                        tv_login_login.setTextColor(Color.parseColor("#ffffff"))
                    }
                    else{
                        cl_login_login.setBackgroundResource(R.drawable.rect_white_blue_24dp)
                        tv_login_login.setTextColor(Color.parseColor("#0057ff"))
                    }
                } else {
                    // 비활성화
                    pwFlag = false
                }
            }
        })
    }
}
