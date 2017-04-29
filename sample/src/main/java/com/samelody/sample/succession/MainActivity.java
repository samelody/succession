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
package com.samelody.sample.succession;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.samelody.succession.Succession;
import com.samelody.succession.target.ViewTarget;

/**
 * The main screen.
 *
 * @author Belin Wu
 */
public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener {

    private TextView text;
    private TextView text2;
    private Button start1;
    private Button start2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_main);
        text = (TextView) findViewById(R.id.text);
        start1 = (Button) findViewById(R.id.start1);
        text2 = (TextView) findViewById(R.id.text2);
        start2 = (Button) findViewById(R.id.start2);
        start1.setOnClickListener(this);
        start2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start1:
                Succession.with(this)
                        .values(254.0f, 489.0f)
                        .duration(2000)
                        .template(R.string.succession_template)
                        .format("##0.00")
                        .on(text);
                break;
            case R.id.start2:
                Succession.with(this)
                        .values(254, 1489)
                        .duration(800)
                        .frequency(0.2f)
                        .format(",###")
                        .on(new ViewTarget<TextView>(text2) {
                            @Override
                            public void setSuccession(CharSequence succession) {
                                getView().setText(succession);
                            }
                        });
                break;
        }
    }
}