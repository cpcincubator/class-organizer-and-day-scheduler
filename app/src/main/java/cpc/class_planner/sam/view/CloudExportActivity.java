package cpc.class_planner.sam.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cpc.class_planner.sam.R;
import cpc.class_planner.sam.viewmodel.BaseActivityViewModel;
import cpc.class_planner.sam.viewmodel.CloudExportActivityViewModel;

// This activity exports the routine to the cloud
public class CloudExportActivity extends AppCompatActivity {
    @BindView(R.id.cloud_export_btn_next)
    Button nextButton;
    @BindView(R.id.cloud_export_text_label)
    TextView textLabel;
    @BindView(R.id.cloud_export_input_field)
    EditText inputField;
    private static int cursorPosition  = 0;
    private String[] fields = {"Year", "Semester", "Department", "Batch", "Section", "Submitter"};
    HashMap<String, String> dataSet = new HashMap<String, String>();
    CloudExportActivityViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_export);
        viewModel = ViewModelProviders.of(this).get(CloudExportActivityViewModel.class);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.cloud_export_btn_next)
    void toggleNext(){

        if(cursorPosition<fields.length) {
            dataSet.put(fields[cursorPosition], inputField.getText().toString());
            inputField.setText("");
            if(cursorPosition+1<fields.length) textLabel.setText(fields[cursorPosition+1]); // code smell
        } else{
            Log.d("TAG", "toggleNext: " + dataSet.toString());

                viewModel.sendPostRequest(dataSet.get(fields[0]),
                        dataSet.get(fields[1]),
                        dataSet.get(fields[2]),
                        dataSet.get(fields[3]),
                        dataSet.get(fields[4]),
                        dataSet.get(fields[5]));
                this.finish();
            Toast.makeText(this, "Routine submitted.", Toast.LENGTH_SHORT).show();

        }

        cursorPosition++; // another code smell
        if(cursorPosition == fields.length) {
            nextButton.setText("Submit");
            textLabel.setText("Your texts are ready to be submitted");
            textLabel.setTextSize(24);
            textLabel.setTextColor(Color.GREEN);
            inputField.setVisibility(View.GONE);
        }
    }
}
