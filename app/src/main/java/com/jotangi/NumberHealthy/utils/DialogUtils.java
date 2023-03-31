package com.jotangi.NumberHealthy.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jotangi.NumberHealthy.R;


public class DialogUtils {

    public static AlertDialog dialogProgress;
    public static AlertDialog appDialog;
    public static AlertDialog dialog;
    private AlertDialog dialogMultiple;
    private AlertDialog dialogFrequency;
    private AlertDialog dialogHours;

    public interface OneButtonClickListener {
        void onButton1Clicked();
    }

    public interface OnBtnClickListener {
        void onCancel();
    }

    public interface cameraListener {
        void onTake();

        void onOpen();

        void onCancel();
    }

    public interface OnSingleClickListener {
        void onCancel();
    }

    public interface OnMultipleClickListener {
        void onOk();

        void onCancel();
    }

    public interface OnFrequencyClickListener {
        void onMinute(int minute);
    }


    public static void showProgress(Activity activity) {

        ViewGroup group = activity.findViewById(android.R.id.content);
        View view = LayoutInflater.from(activity).inflate(
                R.layout.progress_loading, group, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.NewDialog);
        builder.setOnKeyListener((dialog, keyCode, event) -> true);
        builder.setView(view);
        builder.setCancelable(false);

        dialogProgress = builder.create();
        dialogProgress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogProgress.show();
    }

    public static void closeProgress() {

        if (dialogProgress != null) {
            dialogProgress.dismiss();
        }
    }

    public static void showSingle(
            Activity activity,
            String title,
            String content,
            @NonNull OnSingleClickListener listener
    ) {
        showSingle(activity, title, content, "關閉", listener);
    }

    public static void showSingle(
            Activity activity,
            String title,
            String content,
            String closeContent,
            @NonNull OnSingleClickListener listener
    ) {

        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(
                R.layout.dialog_custom, viewGroup, false);

        TextView dialogTitleTV = dialogView.findViewById(R.id.tv_dialog_title);
        TextView dialogContentTV = dialogView.findViewById(R.id.tv_dialog_content);
        ConstraintLayout dialogCloseCL = dialogView.findViewById(R.id.cl_dialog_close);
        TextView dialogCloseContentTV = dialogView.findViewById(R.id.tv_dialog_close_content);

        if (title.isEmpty()) {
            dialogTitleTV.setVisibility(View.GONE);
        } else {
            dialogTitleTV.setText(title);
        }
        dialogContentTV.setText(content);
        dialogCloseContentTV.setText(closeContent);
        dialogCloseCL.setOnClickListener(v -> {
            dialog.dismiss();
            listener.onCancel();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setOnKeyListener((dialog, keyCode, event) -> true);
        builder.setView(dialogView);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        dialog.show();
    }


    public void showMultiple(
            Activity activity,
            String title,
            String content,
            String ok,
            String cancel,
            @NonNull OnMultipleClickListener listener
    ) {

        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(
                R.layout.dialog_multiple, viewGroup, false);

        TextView titleTV = dialogView.findViewById(R.id.tv_dialog_titleL);
        TextView contentTV = dialogView.findViewById(R.id.tv_dialog_content);
        TextView cancelTV = dialogView.findViewById(R.id.tv_dialog_cancel);
        TextView okTV = dialogView.findViewById(R.id.tv_dialog_ok);

        if (title.isEmpty()) {
            titleTV.setVisibility(View.GONE);
        } else {
            titleTV.setText(title);
        }

        contentTV.setText(content);

        okTV.setText(ok);
        okTV.setOnClickListener(v -> {
            dialogMultiple.dismiss();
            listener.onOk();
        });

        cancelTV.setText(cancel);
        cancelTV.setOnClickListener(v -> {
            dialogMultiple.dismiss();
            listener.onCancel();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);
        builder.setCancelable(false);
        dialogMultiple = builder.create();
        dialogMultiple.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        dialogMultiple.show();
    }


    public void showFrequency(
            Activity activity,
            @NonNull OnFrequencyClickListener listener
    ) {

        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(
                R.layout.dialog_frequency, viewGroup, false);

        TextView m5 = dialogView.findViewById(R.id.tv_frequency_5);
        TextView m15 = dialogView.findViewById(R.id.tv_frequency_15);
        TextView m30 = dialogView.findViewById(R.id.tv_frequency_30);
        TextView m45 = dialogView.findViewById(R.id.tv_frequency_45);
        TextView m60 = dialogView.findViewById(R.id.tv_frequency_60);
        TextView cancel = dialogView.findViewById(R.id.tv_frequency_cancel);

        m5.setOnClickListener(v -> {
            listener.onMinute(5);
            dialogFrequency.dismiss();
        });
        m15.setOnClickListener(v -> {
            listener.onMinute(15);
            dialogFrequency.dismiss();
        });
        m30.setOnClickListener(v -> {
            listener.onMinute(30);
            dialogFrequency.dismiss();
        });
        m45.setOnClickListener(v -> {
            listener.onMinute(45);
            dialogFrequency.dismiss();
        });
        m60.setOnClickListener(v -> {
            listener.onMinute(60);
            dialogFrequency.dismiss();
        });
        cancel.setOnClickListener(v -> {
            dialogFrequency.dismiss();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);
        builder.setCancelable(false);
        dialogFrequency = builder.create();
        dialogFrequency.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        dialogFrequency.show();
    }

    public void hoursDialog(Activity activity, String main, String description, @NonNull final cameraListener onCrop) {

        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(
                R.layout.dialog_hours, viewGroup, false);

        TextView lan24 = dialogView.findViewById(R.id.lan24);
        lan24.setText(main);
        TextView lan12 = dialogView.findViewById(R.id.lan12);
        lan12.setText(description);
        TextView lanbye = dialogView.findViewById(R.id.lanBye);

        lan24.setOnClickListener(v -> {
            dialogHours.dismiss();
            onCrop.onTake();
        });
        lan12.setOnClickListener(v -> {
            dialogHours.dismiss();
            onCrop.onOpen();
        });
        lanbye.setOnClickListener(v -> {
            dialogHours.dismiss();
            onCrop.onCancel();
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);
        builder.setCancelable(true);
        dialogHours = builder.create();
        dialogHours.setCanceledOnTouchOutside(false);
        dialogHours.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        dialogHours.show();
    }


    public static void showMyDialog(Activity activity, String tx1, String tx2, String confirmString, @NonNull final OnBtnClickListener listener) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_location, viewGroup, false);
        ConstraintLayout cl = dialogView.findViewById(R.id.layout_L);
        TextView txt2 = dialogView.findViewById(R.id.txt_closeL);
        buildDialog(dialogView, activity, tx1, tx2, confirmString, listener);
        appDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
    }


    public static void buildDialog(View dialogView, Activity activity, String tx1, String tx2, String confirmString, @NonNull final OnBtnClickListener listener) {
        ConstraintLayout cl = dialogView.findViewById(R.id.layout_L);
        TextView txt2 = dialogView.findViewById(R.id.txt_closeL);
        //Button checkBtn    = dialogView.findViewById(R.id.btnCheck);
        ConstraintLayout confirmBtn = dialogView.findViewById(R.id.btn_closeL);
        txt2.setText(confirmString);
        TextView txtTitle = dialogView.findViewById(R.id.titleL);
        txtTitle.setText(tx1);
        TextView txtContextL = dialogView.findViewById(R.id.contextL);
        txtContextL.setText(tx2);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appDialog.dismiss();
                listener.onCancel();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setView(dialogView);
        builder.setCancelable(true);
        // appDialog.setCanceledOnTouchOutside(false);
        appDialog = builder.create();
        appDialog.setCanceledOnTouchOutside(false);
        appDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        appDialog.show();
    }

    public static void CropCamera(Activity activity, String main, String description, @NonNull final cameraListener onCrop) {
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.daialog_img_face, viewGroup, false);
        ConstraintLayout cl = dialogView.findViewById(R.id.laCamera);
        TextView txt2 = dialogView.findViewById(R.id.txt_closeL);
        CropCameraBuid(dialogView, activity, main, description, onCrop);
        appDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
    }

    public static void showMyDialog(ViewGroup viewGroup, int titleid, int messageid, String parm1, int btnlabelid, OneButtonClickListener listener) {
        Context context = viewGroup.getContext();
        String title = (titleid > 0) ? context.getString(titleid) : "";
        String message = (messageid > 0) ? context.getString(messageid, parm1) : "";
        String btnlabel = (btnlabelid > 0) ? context.getString(btnlabelid) : context.getString(R.string.button_confirm);
        showMyDialog(viewGroup, title, message, btnlabel, listener);
    }

    public static void showMyDialog(ViewGroup viewGroup, String title, String message, String btnlabel, OneButtonClickListener listener) {
        View dialogView = createDialogView(R.layout.dialog_one_button, viewGroup, title, message, btnlabel, listener);
        buildDialog2(dialogView, viewGroup.getContext());
        appDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
    }

    public static View createDialogView(int layoutid, ViewGroup viewGroup, String title, String message, String bnlabel, OneButtonClickListener listener) {
        View dialogView = LayoutInflater.from(viewGroup.getContext()).inflate(layoutid, viewGroup, false);

        TextView bnText = dialogView.findViewById(R.id.tv_bn1_label);
        bnText.setText(bnlabel);

        TextView txtTitle = dialogView.findViewById(R.id.tv_title);
        txtTitle.setText(title);

        TextView txtContextL = dialogView.findViewById(R.id.tv_context);
        txtContextL.setText(message);

        ConstraintLayout bnLayout = dialogView.findViewById(R.id.bn1);
        bnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButton1Clicked();
                closeDialog();
            }
        });
        return dialogView;
    }

    public static void buildDialog2(View dialogView, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(dialogView);
        builder.setCancelable(true);
        appDialog = builder.create();
        appDialog.setCanceledOnTouchOutside(false);
        //appDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));

        appDialog.show();
    }

    public static void CropCameraBuid(View dialogView, Activity activity, String tx1, String tx2, @NonNull final cameraListener onCrop) {
        ConstraintLayout la = dialogView.findViewById(R.id.laCamera);
        TextView description = dialogView.findViewById(R.id.titleD);
        TextView main = dialogView.findViewById(R.id.titleC);
        main.setText(tx1);
        description.setText(tx2);
        ConstraintLayout Take = dialogView.findViewById(R.id.btnTake);
        TextView Ga = dialogView.findViewById(R.id.btnGa);
        TextView Cancel = dialogView.findViewById(R.id.btnC);

        Take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appDialog.dismiss();
                onCrop.onTake();
            }
        });
        Ga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appDialog.dismiss();
                onCrop.onOpen();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appDialog.dismiss();
                onCrop.onCancel();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);
        builder.setCancelable(true);
        appDialog = builder.create();
        appDialog.setCanceledOnTouchOutside(false);
        appDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        appDialog.show();
    }

    public static void closeDialog() {
        if (appDialog != null) {
            appDialog.dismiss();
            appDialog = null;
        }
    }

    public static void NotcloseDialog() {
        if (appDialog != null) {
            appDialog.show();

        }
    }
}
