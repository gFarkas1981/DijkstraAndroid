package com.gfarkas.dijkstra;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gfarkas.dijkstra.model.Graph;
import com.gfarkas.dijkstra.model.Node;

import java.util.ArrayList;
import java.util.List;

public class ChildNodesActivity extends AppCompatActivity {

    private Graph graph;
    private Spinner parentSpinner;
    private Spinner childSpinner;
    private ArrayAdapter<String> childArrayAdapter;
    private LinearLayout root;
    private ArrayList<String> allNodes = new ArrayList<>();
    private ArrayList<String> childNodes;
    private int cost;
    private EditText nodeCostToChange;
    private String nodeNameToChange;
    private String selectedPredecessorName;
    private String selectedChildName;
    private TextView selectedChildTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_child_nodes);

        graph = new Graph();
        parentSpinner = findViewById(R.id.parentSpinner);
        childSpinner = findViewById(R.id.childSpinner);
        root = findViewById(R.id.rootLayout);

    }

    @Override
    protected void onResume() {

        Intent intent = getIntent();

        if (intent.hasExtra("graph")) {

            graph = (Graph) getIntent().getSerializableExtra("graph");

        }


        for (Node node : graph.getNodes()) {

            allNodes.add(node.getName());

        }

        parentSpinner.setAdapter(null);
        ArrayAdapter<String> parentArrayAdapter =
                new ArrayAdapter<>(ChildNodesActivity.this, android.R.layout.simple_spinner_item, allNodes);
        parentArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parentSpinner.setAdapter(parentArrayAdapter);
        parentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedPredecessorName = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(),
                        "Selected node: " + selectedPredecessorName, Toast.LENGTH_SHORT).show();
                TextView selectedParentTV = findViewById(R.id.selectedParentTV);
                selectedChildTV = findViewById(R.id.selectedChildTV);
                selectedParentTV.setText(R.string.selected_parent);

                reloadChildList();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });


        super.onResume();

    }

    private void reloadChildList() {

        Node parentNode = new Node();
        childNodes  = new ArrayList<>();

        for (Node node : graph.getNodes()) {

            if (node.getName().equals(selectedPredecessorName)) {

                parentNode.setName(node.getName());
                parentNode.setChildNodes(node.getChildNodes());

            }

        }

        for (Node node : graph.getNodes()) {

            if (!node.getName().equals(parentNode.getName())) {

                if (!parentNode.getChildNodes().containsKey(node)) {

                    childNodes.add(node.getName());

                } else {

                    childNodes.remove(node.getName());

                }

            }

        }

        childSpinner.setAdapter(null);
        childArrayAdapter = new ArrayAdapter<>
                (ChildNodesActivity.this, android.R.layout.simple_spinner_dropdown_item, childNodes);
        childArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        childSpinner.setAdapter(childArrayAdapter);
        childSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedChildName = parent.getItemAtPosition(position).toString();
                selectedChildTV.setText(R.string.selected_child);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

    }

    public void backToMain(View view) {

        try {

            cost = Integer.valueOf(nodeCostToChange.getText().toString());

            if (cost > 0 && cost < Integer.MAX_VALUE) {

                nodeCostToChange.setText(String.valueOf(cost));

                for (Node graphNode : graph.getNodes()) {

                    if (graphNode.getName().equals(selectedPredecessorName)) {

                        Node childNode = null;

                        for (Node node : graph.getNodes()) {

                            if (node.getName().equals(nodeNameToChange)) {

                                childNode = node;
                                childNode.setPredecessor(graphNode);

                            }

                        }

                        if (childNode != null) {

                            graphNode.getChildNodes().put(childNode, cost);

                        }

                    }

                }


            }

        } catch (NumberFormatException nfe) {

            nodeCostToChange.setText(String.valueOf(cost));
            Toast.makeText(ChildNodesActivity.this,
                    "Enter integer please!", Toast.LENGTH_LONG).show();

        }

        Intent mainActivityIntent = new Intent();
        mainActivityIntent.putExtra("graph", graph);
        setResult(Activity.RESULT_OK, mainActivityIntent);
        finish();

    }

    public void addChild(View view) {

        boolean existingChild = false;

        for (Node node : graph.getNodes()) {

            if (node.getName().equals(selectedPredecessorName)) {

                for (Node childNode : node.getChildNodes().keySet()) {

                    if (childNode.getName().equals(selectedChildName)) {

                        existingChild = true;

                    }

                }

                if (!existingChild) {


                    Node newChild = new Node(selectedChildName);
                    newChild.setPredecessor(node);
                    node.addChildNode(newChild, Integer.MAX_VALUE);


                    childNodes.clear();

                    for (Node childNode : node.getChildNodes().keySet()) {

                        childNodes.add(childNode.getName());
                        System.out.println("parent: " + childNode.getPredecessor().getName());
                        System.out.println("child: " + childNode.getName());

                    }

                    reloadChildList();
                    refreshChildListOnScreen(node);

                }

            }

        }

    }

    public List<View> getAllViews(ViewGroup layout) {

        List<View> views = new ArrayList<>();

        for (int i = 0; i < layout.getChildCount(); i++) {

            views.add(layout.getChildAt(i));

        }

        return views;

    }

    public void deleteChildrenFromScreen() {

        List<View> views = getAllViews(root);
        List<View> views2;
        List<View> views3;

        for (View v : views) {


                System.out.println("linlay: " + v.getId());


        }

    }

    public void refreshChildListOnScreen(Node parent) {

        deleteChildrenFromScreen();
        int linearLayoutID = 1000;

        for (final Node childNode : parent.getChildNodes().keySet()) {

            LayoutInflater inflater = LayoutInflater.from(root.getContext());
            LinearLayout nodeElement = (LinearLayout) inflater.inflate(R.layout.childnode, root, false);

            root.addView(nodeElement, 6);

            LinearLayout linearLayout = findViewById(R.id.childNodeLinearLayout);
            linearLayout.setId(linearLayoutID);

            TextView nodeNameTV = findViewById(R.id.textView);
            nodeNameTV.setText(childNode.getName());
            nodeNameTV.setId(linearLayoutID + 1000);

            final EditText nodeCost = findViewById(R.id.editText);
            nodeCost.setText(childNode.getCost() + "");
            nodeCost.setId(linearLayoutID + 2000);

            nodeCost.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View view, boolean hasFocus) {

                    if (hasFocus) {

                        cost = Integer.valueOf(nodeCost.getText().toString());
                        nodeCostToChange = nodeCost;
                        nodeNameToChange = childNode.getName();

                    } else {

                        try {

                            cost = Integer.valueOf(nodeCost.getText().toString());

                            if (cost > 0 && cost < Integer.MAX_VALUE) {

                                nodeCost.setText(String.valueOf(cost));

                                for (Node graphNode : graph.getNodes()) {

                                    if (graphNode.getName().equals(selectedPredecessorName)) {

                                        childNode.setPredecessor(graphNode);
                                        graphNode.getChildNodes().put(childNode, cost);

                                    }

                                }


                            }

                        } catch (NumberFormatException nfe) {

                            nodeCost.setText(String.valueOf(cost));
                            Toast.makeText(ChildNodesActivity.this,
                                    "Enter integer please!", Toast.LENGTH_LONG).show();

                        }


                    }
                }
            });


            linearLayoutID++;


        }

    }

}
