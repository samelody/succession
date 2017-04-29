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
package com.samelody.succession.target;

import android.view.View;

import static com.samelody.succession.Utils.requireNonNull;

/**
 * The {@link Target} abstract implementation for displaying succession in {@link View}s.
 *
 * @author Belin Wu
 */
public abstract class ViewTarget<V extends View> implements Target {

    /**
     * The view.
     */
    private final V view;

    public ViewTarget(V view) {
        this.view = requireNonNull(view, "view must not be null");
    }

    /**
     * Gets the view.
     *
     * @return The view
     */
    protected V getView() {
        return view;
    }
}
