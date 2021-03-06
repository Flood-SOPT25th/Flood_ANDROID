package com.flood_android.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.flood_android.R
import kotlinx.android.synthetic.main.fragment_signup2.*
import kotlinx.android.synthetic.main.fragment_signup3.*

class SignupFragment_3 : Fragment() {

    var questionFlag = false
    var answerFlag = false

    private lateinit var questionDialog: SignupQuestionDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup3, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        questionDialog = SignupQuestionDialog {
            tv_signup3_question_2.text = it
        }

        tv_signup3_question_2.setOnClickListener {
            questionDialog.show(activity!!.supportFragmentManager, null)
        }

        tv_signup3_question_2.addTextChangedListener(questionWatcher)
        edtxt_signup3_answer_2.addTextChangedListener(answerWatcher)
    }

    fun toSignal(flag: Boolean) {
        (activity as SignupActivity).activateNextBtn(flag)
    }

    private val questionWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if ((s ?: "").isNotEmpty()) {
                questionFlag = true
                if (answerFlag) {
                    toSignal(true)
                    toAct3()
                }
                else
                    toSignal(false)
            } else {
                toSignal(false)
                questionFlag = false
            }
        }
    }
    private val answerWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if ((s ?: "").isNotEmpty()) {
                answerFlag = true
                if (questionFlag) {
                    toSignal(true)
                    toAct3()
                }
                else
                    toSignal(false)
            } else {
                toSignal(false)
                answerFlag = false
            }
        }
    }

    fun toAct3() {
        (activity as SignupActivity).signupInfo.question = tv_signup3_question_2.text.toString()
        (activity as SignupActivity).signupInfo.answer = edtxt_signup3_answer_2.text.toString()
    }
}




