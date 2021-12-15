package com.example.rssstackoverflow

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rssstackoverflow.databinding.QuestionRowBinding
import android.widget.TextView
import androidx.core.view.marginEnd
import android.widget.LinearLayout
import androidx.core.view.setPadding


class QuestionAdapter(private var questions: ArrayList<Question>, val context: Context): RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {
    class QuestionViewHolder(val binding: QuestionRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder(QuestionRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.binding.apply {
            tvTitle.text = question.title
            tvAuthor.text = question.author
            tvPublished.text = question.published
            cvQuestion.setOnClickListener {
                CustomAlert(context, question.title, question.summary)
            }

            val N = question.category.size // total number of textviews to add
            val myTextViews = arrayOfNulls<TextView>(N) // create an empty array;
            for (i in 0 until N) {
                // create a new textview
                val rowTextView = TextView(context)
                // set some properties of rowTextView or something
                rowTextView.setTextColor(Color.BLACK)
                rowTextView.setBackgroundColor(Color.parseColor("#d3d3d3"))
                rowTextView.setPadding(10)
                val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(10,10,10,10)
                rowTextView.setLayoutParams(params)

//                rowTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18f)
                rowTextView.text = "${question.category[i]}" //" ${question.category[i]} "
                // add the textview to the linearlayout
                llCategory.addView(rowTextView)
                // save a reference to the textview for later
                myTextViews[i] = rowTextView
            }
        }
    }

    override fun getItemCount() = questions.size

    fun update(newQuestions: ArrayList<Question>){
        questions = newQuestions
        notifyDataSetChanged()
    }
}