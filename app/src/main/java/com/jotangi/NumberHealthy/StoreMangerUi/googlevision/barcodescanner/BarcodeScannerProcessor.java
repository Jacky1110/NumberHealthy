/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jotangi.NumberHealthy.StoreMangerUi.googlevision.barcodescanner;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.jotangi.NumberHealthy.StoreMangerUi.googlevision.GraphicOverlay;
import com.jotangi.NumberHealthy.StoreMangerUi.googlevision.VisionProcessorBase;
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil;

import java.util.List;

/**
 * Barcode Detector Demo.
 */
public class BarcodeScannerProcessor extends VisionProcessorBase<List<Barcode>> {

    private static final String TAG = "BarcodeProcessor";

    private final BarcodeScanner barcodeScanner;

    private BarcodeFoundListener barcodeFoundListener;

    public interface BarcodeFoundListener {
        void onFound(List<Barcode> barcodes);
    }

    public BarcodeScannerProcessor(Context context, BarcodeFoundListener listener) {
        super(context);
        // Note that if you know which format of barcode your app is dealing with, detection will be
        // faster to specify the supported barcode formats one by one, e.g.
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build();
        barcodeScanner = BarcodeScanning.getClient(options);

        barcodeFoundListener = listener;
    }

    @Override
    public void stop() {
        super.stop();
        barcodeScanner.close();
    }

    @Override
    protected Task<List<Barcode>> detectInImage(InputImage image) {
        return barcodeScanner.process(image);
    }

    @Override
    protected void onFailure(@NonNull Exception e) {

    }

    private void logExtrasForTesting(Barcode barcode) {
        if (barcode != null) {
            if (barcode.getBoundingBox() != null) {
            }
            if (barcode.getCornerPoints() != null) {

            }
            for (Point point : barcode.getCornerPoints()) {

            }

            Barcode.DriverLicense dl = barcode.getDriverLicense();
            if (dl != null) {

            }
        }

    }

    @Override
    protected void onSuccess(
            @NonNull List<Barcode> barcodes, @NonNull GraphicOverlay graphicOverlay) {
        if (barcodes.isEmpty()) {
            Log.v(MANUAL_TESTING_LOG, "No barcode has been detected");
            return;
        }
        for (int i = 0; i < barcodes.size(); ++i) {
            Barcode barcode = barcodes.get(i);
            graphicOverlay.add(new BarcodeGraphic(graphicOverlay, barcode));
            Log.v("Kelly find ", "Find barcode " + barcode.getRawValue());
            if (barcode.getRawValue().contains("member_id=")) {
                SharedPreferencesUtil.Companion.getInstances().setSB(barcode.getRawValue());
            }
            logExtrasForTesting(barcode);
        }
        if (barcodeFoundListener != null) {
            barcodeFoundListener.onFound(barcodes);
        }
    }


}
