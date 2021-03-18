/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.CodingErgo.sticker.StickerManager;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.CodingErgo.sticker.R;
import com.facebook.drawee.view.SimpleDraweeView;

class StickerPreviewViewHolder extends RecyclerView.ViewHolder {

    final SimpleDraweeView stickerPreviewView;

    StickerPreviewViewHolder(final View itemView) {
        super(itemView);
        stickerPreviewView = itemView.findViewById(R.id.sticker_preview);
    }
}