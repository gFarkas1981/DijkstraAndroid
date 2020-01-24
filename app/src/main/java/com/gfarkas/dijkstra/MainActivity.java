package com.gfarkas.dijkstra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gfarkas.dijkstra.model.Graph;
import com.gfarkas.dijkstra.model.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private LinearLayout root;
    private int addButtonID = 0;
    private int removeButtonID = 1000;
    private int layoutID = 2000;
    private int nodes = 0;
    private Graph graph;
    private Map<String, String> oldNewNodeNames;
    private EditText nameET;
    private String oldName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        root = findViewById(R.id.rootLayout);

        graph = new Graph();
        oldNewNodeNames = new HashMap<>();
        addNode();
        addNode();
        addNode();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {


            assert data != null;
            if (data.hasExtra("graph")) {

                this.graph = (Graph) data.getSerializableExtra("graph");

                for (final Node node : graph.getNodes()) {

                    for (Node node1 : node.getChildNodes().keySet()) {

                        Log.i("Child's name: ", node1.getName());
                        Log.i("Child's cost: ", node1.getCost() + "");
                        if (node1.getPredecessor() != null) {
                            Log.i("Child's predecessor: ", node1.getPredecessor().getName());
                        }

                    }

                }

            }


        }

    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    private void addNode() {

        if (nodes < 10) {

            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout nodeElement = (LinearLayout) inflater.inflate(R.layout.newnode, root, false);
            nodeElement.setId(layoutID);
            final int actualLayoutId = layoutID;
            root.addView(nodeElement, 0);
            Button addButton = findViewById(R.id.add_btn);
            addButton.setId(addButtonID);
            addButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    addNode();

                }
            });

            Button removeButton = findViewById(R.id.delete_btn);
            removeButton.setId(removeButtonID);
            removeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    removeNode(actualLayoutId);

                }
            });

            final int nameID = layoutID + 5000;
            nameET = findViewById(R.id.editText);

            nameET.setText("N_" + layoutID);
            oldNewNodeNames.put("N_" + layoutID, "N_" + layoutID);
            nameET.setId(nameID);

            nameET.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    oldName = nameET.getText().toString();

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {

                    oldNewNodeNames.put(oldName, s.toString());

                }

            });

            layoutID++;
            addButtonID++;
            removeButtonID++;
            nodes++;

            Node nodeToAdd = new Node(nameET.getText().toString());
            graph.getNodes().add(nodeToAdd);

        }

    }

    public List<View> getAllViews(ViewGroup layout) {

        List<View> views = new ArrayList<>();

        for (int i = 0; i < layout.getChildCount(); i++) {

            views.add(layout.getChildAt(i));

        }

        return views;

    }

    private boolean areNodeNamesUnique() {

        int found = 0;

        for (String oldName : oldNewNodeNames.keySet()) {

            for (String oldName2 : oldNewNodeNames.keySet()) {

                if (oldNewNodeNames.get(oldName2).equals(oldName)) {

                    found++;
                    System.out.println("talált: " + oldName);

                }

            }

        }

        return true;

    }

    public void toChildNodesActivity(View view) {

        if (areNodeNamesUnique()) {

            Intent childNodesIntent = new Intent(this, ChildNodesActivity.class);
            childNodesIntent.putExtra("graph", graph);
            startActivityForResult(childNodesIntent, 1);

        }

    }

    private void removeNode(int id) {

        if (nodes > 3) {

            LinearLayout linearLayout = findViewById(id);

            EditText nameEditText = findViewById(id + 5000);

            String nodeName = nameEditText.getText().toString();

            Iterator<Node> itr = graph.getNodes().iterator();

            while (itr.hasNext()) {

                Node node = itr.next();

                if (node.getName().equals(nodeName)) {

                    itr.remove();

                }

            }

            root.removeView(linearLayout);
            nodes--;

        }

    }

}
