/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.CodingErgo.sticker.StickerManager;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CodingErgo.sticker.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static android.content.ContentValues.TAG;

public class StickerPreviewAdapter extends RecyclerView.Adapter<StickerPreviewViewHolder> {

    private static final float COLLAPSED_STICKER_PREVIEW_BACKGROUND_ALPHA = 1f;
    private static final float EXPANDED_STICKER_PREVIEW_BACKGROUND_ALPHA = 0.2f;

    @NonNull
    private final StickerPack stickerPack;

    private final int cellSize;
    private final int cellLimit;
    private final int cellPadding;
    private final int errorResource;
    private final SimpleDraweeView expandedStickerPreview;

    private final LayoutInflater layoutInflater;
    private RecyclerView recyclerView;
    private View clickedStickerPreview;
    float expandedViewLeftX;
    float expandedViewTopY;

    StickerPreviewAdapter(
            @NonNull final LayoutInflater layoutInflater,
            final int errorResource,
            final int cellSize,
            final int cellPadding,
            @NonNull final StickerPack stickerPack,
            final SimpleDraweeView expandedStickerView) {
        this.cellSize = cellSize;
        this.cellPadding = cellPadding;
        this.cellLimit = 0;
        this.layoutInflater = layoutInflater;
        this.errorResource = errorResource;
        this.stickerPack = stickerPack;
        this.expandedStickerPreview = expandedStickerView;

    }

    @NonNull
    @Override
    public StickerPreviewViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        View itemView = layoutInflater.inflate(R.layout.sticker_image_item, viewGroup, false);
        StickerPreviewViewHolder vh = new StickerPreviewViewHolder(itemView);

        ViewGroup.LayoutParams layoutParams = vh.stickerPreviewView.getLayoutParams();
        layoutParams.height = cellSize;
        layoutParams.width = cellSize;
        vh.stickerPreviewView.setLayoutParams(layoutParams);
        vh.stickerPreviewView.setPadding(cellPadding, cellPadding, cellPadding, cellPadding);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final StickerPreviewViewHolder stickerPreviewViewHolder, final int i) {
        stickerPreviewViewHolder.stickerPreviewView.setImageResource(errorResource);
        stickerPreviewViewHolder.stickerPreviewView.setImageURI(StickerPackLoader.getStickerAssetUri(stickerPack.identifier, stickerPack.getStickers().get(i).imageFileName));
        stickerPreviewViewHolder.stickerPreviewView.setOnClickListener(v -> expandPreview(i, stickerPreviewViewHolder.stickerPreviewView));
        stickerPreviewViewHolder.stickerPreviewView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(stickerPreviewViewHolder.itemView.getContext());
                dialog.setContentView(R.layout.stickerpre);
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                layoutParams.height = 1200;
                layoutParams.width = 1000;
                dialog.getWindow().setAttributes(layoutParams);
                dialog.show();
                try{
                    final  Uri uri = StickerPackLoader.getStickerAssetUri(stickerPack.identifier, stickerPack.getStickers().get(i).imageFileName);
                    ImageView imageView = dialog.findViewById(R.id.stickerpreimage);
                    ImageView cancleDialog = dialog.findViewById(R.id.cancleDialog);
                    imageView.setImageURI(uri);
                    Button share , save ;
                   save = dialog.findViewById(R.id.dialogSave);
                   share = dialog.findViewById(R.id.dialogShare);
                   save.setText("Close");
                   share.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Intent intent = new Intent(Intent.ACTION_SEND);
                           intent.setType("images/*");
                           intent.putExtra(Intent.EXTRA_STREAM , uri);
                           intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                           stickerPreviewViewHolder.itemView.getContext().startActivity(intent);
                           Log.d("TAG", "onClickStickerwala: "+uri);
                       }
                   });
                   save.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           dialog.dismiss();


                       }
                   });
                    cancleDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(stickerPreviewViewHolder.itemView.getContext(), "Error", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.addOnScrollListener(hideExpandedViewScrollListener);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView.removeOnScrollListener(hideExpandedViewScrollListener);
        this.recyclerView = null;
    }

    private final RecyclerView.OnScrollListener hideExpandedViewScrollListener =
            new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dx != 0 || dy != 0) {
                        hideExpandedStickerPreview();
                    }
                }
            };

    private void positionExpandedStickerPreview(int selectedPosition) {
        if (expandedStickerPreview != null) {
            // Calculate the view's center (x, y), then use expandedStickerPreview's height and
            // width to
            // figure out what where to position it.
            final ViewGroup.MarginLayoutParams recyclerViewLayoutParams =
                    ((ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams());
            final int recyclerViewLeftMargin = recyclerViewLayoutParams.leftMargin;
            final int recyclerViewRightMargin = recyclerViewLayoutParams.rightMargin;
            final int recyclerViewWidth = recyclerView.getWidth();
            final int recyclerViewHeight = recyclerView.getHeight();

            final StickerPreviewViewHolder clickedViewHolder =
                    (StickerPreviewViewHolder)
                            recyclerView.findViewHolderForAdapterPosition(selectedPosition);
            if (clickedViewHolder == null) {
                hideExpandedStickerPreview();
                return;
            }
            clickedStickerPreview = clickedViewHolder.itemView;
            final float clickedViewCenterX =
                    clickedStickerPreview.getX()
                            + recyclerViewLeftMargin
                            + clickedStickerPreview.getWidth() / 2f;
            final float clickedViewCenterY =
                    clickedStickerPreview.getY() + clickedStickerPreview.getHeight() / 2f;

            expandedViewLeftX = clickedViewCenterX - expandedStickerPreview.getWidth() / 2f;
            expandedViewTopY = clickedViewCenterY - expandedStickerPreview.getHeight() / 2f;

            // If the new x or y positions are negative, anchor them to 0 to avoid clipping
            // the left side of the device and the top of the recycler view.
            expandedViewLeftX = Math.max(expandedViewLeftX, 0);
            expandedViewTopY = Math.max(expandedViewTopY, 0);

            // If the bottom or right sides are clipped, we need to move the top left positions
            // so that those sides are no longer clipped.
            final float adjustmentX =
                    Math.max(
                            expandedViewLeftX
                                    + expandedStickerPreview.getWidth()
                                    - recyclerViewWidth
                                    - recyclerViewRightMargin,
                            0);
            final float adjustmentY =
                    Math.max(expandedViewTopY + expandedStickerPreview.getHeight() - recyclerViewHeight, 0);

            expandedViewLeftX -= adjustmentX;
            expandedViewTopY -= adjustmentY;


            expandedStickerPreview.setX(expandedViewLeftX);
            expandedStickerPreview.setY(expandedViewTopY);
        }
    }

    private void expandPreview(int position, View clickedStickerPreview) {
        if (isStickerPreviewExpanded()) {
            hideExpandedStickerPreview();
            return;
        }

        this.clickedStickerPreview = clickedStickerPreview;
        if (expandedStickerPreview != null) {
            positionExpandedStickerPreview(position);

            final Uri stickerAssetUri = StickerPackLoader.getStickerAssetUri(stickerPack.identifier, stickerPack.getStickers().get(position).imageFileName);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(stickerAssetUri)
                    .setAutoPlayAnimations(true)
                    .build();
            expandedStickerPreview.setImageResource(errorResource);
            expandedStickerPreview.setController(controller);

            clickedStickerPreview.setVisibility(View.INVISIBLE);
            expandedStickerPreview.setVisibility(View.VISIBLE);
            recyclerView.setAlpha(EXPANDED_STICKER_PREVIEW_BACKGROUND_ALPHA);

            expandedStickerPreview.setOnClickListener(v -> hideExpandedStickerPreview());
        }
    }

    public void hideExpandedStickerPreview() {
        if (isStickerPreviewExpanded() && expandedStickerPreview != null) {
            clickedStickerPreview.setVisibility(View.VISIBLE);
            expandedStickerPreview.setVisibility(View.INVISIBLE);
            recyclerView.setAlpha(COLLAPSED_STICKER_PREVIEW_BACKGROUND_ALPHA);
        }
    }

    private boolean isStickerPreviewExpanded() {
        return expandedStickerPreview != null && expandedStickerPreview.getVisibility() == View.VISIBLE;
    }

    @Override
    public int getItemCount() {
        int numberOfPreviewImagesInPack;
        numberOfPreviewImagesInPack = stickerPack.getStickers().size();
        if (cellLimit > 0) {
            return Math.min(numberOfPreviewImagesInPack, cellLimit);
        }
        return numberOfPreviewImagesInPack;
    }
}
