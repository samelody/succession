/*
 * Copyright (c) 2017-present Samelody.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package com.samelody.succession;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.FloatRange;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.samelody.succession.target.Target;
import com.samelody.succession.target.TextViewTarget;

import java.text.DecimalFormat;

import static android.text.TextUtils.isEmpty;
import static com.samelody.succession.Utils.requireNonNull;
import static java.lang.String.valueOf;

/**
 * The Succession represents a animated succession of numbers.
 *
 * @author Belin Wu
 */
public final class Succession {

    /**
     * The application context for getting string resources.
     */
    private Context context;

    /**
     * The weak reference of the target view.
     */
    private Target target;

    /**
     * The animator of the succession.
     */
    private ObjectAnimator animator;

    /**
     * The template of the succession.
     */
    @StringRes
    private int template;

    /**
     * The decimal format.
     */
    private DecimalFormat format;

    /**
     * The decimal format pattern.
     */
    private String pattern;

    /**
     * The start value.
     */
    private float start;

    /**
     * The end value.
     */
    private float end;

    /**
     * The fraction of the succession.
     */
    private float fraction;

    /**
     * The frequency fo the succession.
     */
    private float frequency;

    /**
     * The integral flag.
     */
    private boolean integral;

    /**
     * The succession listener.
     */
    private SuccessionListener listener;

    /**
     * Constructs a succession.
     *
     * @param context The context.
     */
    private Succession(Context context) {
        this.context = context.getApplicationContext();
        animator = ObjectAnimator.ofFloat(this, "value", start, end);
        animator.addListener(new SuccessionAnimatorListener());
    }

    /**
     * Sets the animated value.
     *
     * @param value The animated value.
     */
    private void setValue(float value) {
        if (!inFrequency()) {
            return;
        }
        String succession = integral ? valueOf((int) value) : valueOf(value);
        if (format != null) {
            succession = format.format(value);
        }
        if (template != 0) {
            succession = context.getString(template, succession);
        }
        target.setSuccession(succession);
    }

    /**
     * Checks if in the frequency or not.
     *
     * @return true: yes, false: no.
     */
    private boolean inFrequency() {
        if (frequency == 0f) {
            return true;
        }
        float fraction = animator.getAnimatedFraction();
        if (fraction - this.fraction < frequency
                && fraction > 0.0f
                && fraction < 1.0f) {
            return false;
        }
        this.fraction = fraction;
        return true;
    }

    /**
     * Sets the amount of time, in milliseconds, to delay starting the succession.
     *
     * @param delay The amount of the delay, in milliseconds
     * @return this
     */
    public Succession delay(long delay) {
        animator.setStartDelay(delay);
        return this;
    }

    public Succession duration(long duration) {
        animator.setDuration(duration);
        return this;
    }

    public Succession interpolator(TimeInterpolator interpolator) {
        animator.setInterpolator(interpolator);
        return this;
    }

    public Succession template(@StringRes int template) {
        this.template = template;
        return this;
    }

    /**
     * Sets the frequency of the succession.
     *
     * @param frequency The frequency of the succession
     * @return This succession.
     */
    public Succession frequency(@FloatRange(from = 0.0, to = 1.0) float frequency) {
        this.frequency = frequency;
        return this;
    }

    /**
     * Sets the pattern of decimal format.
     *
     * @param pattern The pattern of decimal format.
     * @return This succession.
     * @see DecimalFormat#DecimalFormat(String)
     */
    public Succession format(String pattern) {
        this.pattern = pattern;
        return this;
    }

    public Succession values(int start, int end) {
        integral = true;
        setFloatValues(start, end);
        return this;
    }

    public Succession values(float start, float end) {
        integral = false;
        setFloatValues(start, end);
        return this;
    }

    /**
     * Sets the succession listener.
     *
     * @param listener The succession listener
     */
    public Succession listener(SuccessionListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * Sets the target of this succession and starts the succession.
     *
     * @param view The {@link TextView} that displays the succession.
     */
    public void on(TextView view) {
        on(new TextViewTarget(view));
    }

    /**
     * Sets the target of this succession and starts the succession.
     *
     * @param target The {@link Target} that displays the succession.
     * @param <T>    The type of target.
     */
    public <T extends Target> void on(T target) {
        this.target = requireNonNull(target, "The target must not be null!");
        start();
    }

    /**
     * Creates a succession with the given {@link Context}.
     *
     * @return The new succession.
     */
    public static Succession with(Context context) {
        requireNonNull(context, "The context must not be null!");
        return new Succession(context);
    }

    /**
     * Creates a succession with the given {@link Fragment}.
     *
     * @param fragment The fragment.
     * @return The new succession.
     */
    public static Succession with(Fragment fragment) {
        requireNonNull(fragment, "The fragment must not be null!");
        return with(fragment.getContext());
    }

    /**
     * Creates a succession with the given {@link android.app.Fragment}.
     *
     * @param fragment The fragment.
     * @return The new succession.
     */
    public static Succession with(android.app.Fragment fragment) {
        requireNonNull(fragment, "The fragment must not be null!");
        return with((Context) fragment.getActivity());
    }

    /**
     * Creates a succession with the given {@link Activity}.
     *
     * @param activity The activity.
     * @return The new succession.
     */
    public static Succession with(Activity activity) {
        return with((Context) activity);
    }

    /**
     * Sets the start and end values.
     *
     * @param start The start value.
     * @param end   The end value.
     */
    private void setFloatValues(float start, float end) {
        this.start = start;
        this.end = end;
        animator.setFloatValues(start, end);
    }

    /**
     * Starts the succession.
     */
    private void start() {
        if (this.start == 0 && this.end == 0) {
            return;
        }
        if (!isEmpty(pattern)) {
            this.format = new DecimalFormat(pattern);
        }
        animator.start();
    }

    /**
     * Returns the succession listener.
     *
     * @return The succession listener
     */
    private SuccessionListener getSuccessionListener() {
        if (listener == null) {
            listener = new SimpleSuccessionListener();
        }
        return listener;
    }

    /**
     * Represents the animator listener for succession.
     */
    private class SuccessionAnimatorListener extends AnimatorListenerAdapter {

        @Override
        public void onAnimationStart(Animator animation) {
            getSuccessionListener().onSuccessionStart();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            getSuccessionListener().onSuccessionEnd();
        }
    }
}
