package com.changshi.issa;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.List;

public class ClickableTextView extends AppCompatTextView {
    private OnSubCategoryClickListener onSubCategoryClickListener;

    public ClickableTextView(Context context) {
        super(context);
        init();
    }

    public ClickableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClickableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setOnSubCategoryClickListener(OnSubCategoryClickListener listener) {
        this.onSubCategoryClickListener = listener;
    }

    public interface OnSubCategoryClickListener {
        void onSubCategoryClick(String subCategory);

        void onSubCategoryClick(String subCategory, int position);
    }

    public void setTextWithClickableSubCategories(List<String> subCategories) {
        SpannableString spannableString = new SpannableString(TextUtils.join(", ", subCategories));

        for (int i = 0; i < subCategories.size(); i++) {
            final String subCategory = subCategories.get(i);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    if (onSubCategoryClickListener != null) {
                        onSubCategoryClickListener.onSubCategoryClick(subCategory);
                    }
                }
            };

            spannableString.setSpan(clickableSpan, spannableString.toString().indexOf(subCategory),
                    spannableString.toString().indexOf(subCategory) + subCategory.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        setText(spannableString);
    }
}