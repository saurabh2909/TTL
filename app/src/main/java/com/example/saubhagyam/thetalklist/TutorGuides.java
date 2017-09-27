package com.example.saubhagyam.thetalklist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.saubhagyam.thetalklist.TutorGuideFiles.ForthLevelHolder;
import com.example.saubhagyam.thetalklist.TutorGuideFiles.HeaderHolder;
import com.example.saubhagyam.thetalklist.TutorGuideFiles.IconTreeItemHolder;
import com.example.saubhagyam.thetalklist.TutorGuideFiles.SecondLevelHolder;
import com.example.saubhagyam.thetalklist.TutorGuideFiles.ThirdLevelHolder;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;

public class TutorGuides extends Fragment implements TreeNode.TreeNodeClickListener {

    ExpandableLinearLayout tutor_guide_language_layout, tutor_guide_categories_layout;
    LinearLayout tutor_guides_categories_11, tutor_guides_language_11;
    ImageView tutor_guide_Language_btn, tutor_guide_categories_btn;
    int language_bit, categories_bit;


    private AndroidTreeView tView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutor_guides, container, false);

     /*   tutor_guides_categories_11 = (LinearLayout) view.findViewById(R.id.tutor_guides_categories_11);
        tutor_guides_language_11 = (LinearLayout) view.findViewById(R.id.tutor_guide_language_11);

        tutor_guide_categories_btn= (ImageView) view.findViewById(R.id.tutor_guide_categories_btn);
        tutor_guide_Language_btn= (ImageView) view.findViewById(R.id.tutor_guide_Language_btn);

        tutor_guide_language_layout = (ExpandableLinearLayout) view.findViewById(R.id.tutor_guide_language_layout);
        tutor_guide_categories_layout = (ExpandableLinearLayout) view.findViewById(R.id.tutor_guide_categories_layout);




        tutor_guides_categories_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tutor_guide_language_layout.isExpanded()) {
                    tutor_guide_language_layout.collapse();
                    tutor_guide_categories_btn.setImageResource(R.drawable.side_aerrow);
                    language_bit = 0;
                }
                tutor_guide_categories_layout.toggle();
                if (categories_bit == 0) {
                    tutor_guide_categories_btn.setImageResource(R.drawable.down_aerrow);
                    categories_bit = 1;
                } else {
                    tutor_guide_categories_btn.setImageResource(R.drawable.side_aerrow);
                    categories_bit = 0;
                }
            }
        });
        tutor_guides_language_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tutor_guide_categories_layout.isExpanded()) {
                    tutor_guide_categories_layout.collapse();
                    tutor_guide_Language_btn.setImageResource(R.drawable.side_aerrow);
                    categories_bit = 0;
                }
                tutor_guide_language_layout.toggle();
                if (language_bit == 0) {
                    tutor_guide_Language_btn.setImageResource(R.drawable.down_aerrow);
                    language_bit = 1;
                } else {
                    tutor_guide_Language_btn.setImageResource(R.drawable.side_aerrow);
                    language_bit= 0;
                }
            }
        });
*/


        String Json = "[\n" +
                "  {\n" +
                "    \"Name\": \"Language\",\n" +
                "    \"Language\": [\n" +
                "      {\n" +
                "        \"Name\": \"Leval 2 child 1\",\n" +
                "        \"Leval 2 child 1\": [\n" +
                "          {\n" +
                "            \"Name\": \"level 3 child 1\",\n" +
                "            \"level 3 child 1\": [\n" +
                "              \"abc forth level child 1\",\n" +
                "              \"abc forth level child 1\"\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"Name\": \"level 3 child 1\",\n" +
                "            \"level 3 child 1\": [\n" +
                "              \"abc forth level child 1\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"Name\": \"Leval 2 child 1\",\n" +
                "        \"Leval 2 child 1\": [\n" +
                "          {\n" +
                "            \"Name\": \"level 3 child 1\",\n" +
                "            \"level 3 child 1\": [\n" +
                "              \"abc forth level child 1\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"Name\": \"Language\",\n" +
                "    \"Language\": [\n" +
                "      {\n" +
                "        \"Name\": \"Leval 2 child 1\",\n" +
                "        \"Leval 2 child 1\": [\n" +
                "          {\n" +
                "            \"Name\": \"level 3 child 1\",\n" +
                "            \"level 3 child 1\": [\n" +
                "              \"abc forth level child 1\",\n" +
                "              \"abc forth level child 1\"\n" +
                "            ]\n" +
                "          },\n" +
                "          {\n" +
                "            \"Name\": \"level 3 child 1\",\n" +
                "            \"level 3 child 1\": [\n" +
                "              \"abc forth level child 1\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"Name\": \"Leval 2 child 1\",\n" +
                "        \"Leval 2 child 1\": [\n" +
                "          {\n" +
                "            \"Name\": \"level 3 child 1\",\n" +
                "            \"level 3 child 1\": [\n" +
                "              \"abc forth level child 1\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]";


        TreeNode root = TreeNode.root();
        try {
            JSONArray ary = new JSONArray(Json);

            for (int i = 0; i < ary.length(); i++) {
                JSONObject obj = ary.getJSONObject(i);
//                Toast.makeText(getContext(), "root object length " + obj.length(), Toast.LENGTH_SHORT).show();

                String levelOneName = obj.getString("Name");

                IconTreeItemHolder.IconTreeItem firstLevel = new IconTreeItemHolder.IconTreeItem(levelOneName);
                TreeNode levelOneChild0 = new TreeNode(firstLevel).setViewHolder(new HeaderHolder(getContext()));
                root.addChild(levelOneChild0);


                JSONArray firstLevelAry = obj.getJSONArray(levelOneName);
//                Toast.makeText(getContext(), "first level ary length " + firstLevelAry.length(), Toast.LENGTH_SHORT).show();

                for (int j = 0; j < firstLevelAry.length(); j++) {
                    JSONObject secondObj = firstLevelAry.getJSONObject(j);

                    String secondLevelName = secondObj.getString("Name");
                    IconTreeItemHolder.IconTreeItem twoTreeItem1 = new IconTreeItemHolder.IconTreeItem(secondLevelName);
                    TreeNode levelTwooneChild0 = new TreeNode(twoTreeItem1);
                    levelTwooneChild0.setViewHolder(new SecondLevelHolder(getApplicationContext()));
                    levelOneChild0.addChild(levelTwooneChild0);


                    JSONArray SecondLevelAry = secondObj.getJSONArray(secondLevelName);
//                    Toast.makeText(getContext(), "first level ary length " + firstLevelAry.length(), Toast.LENGTH_SHORT).show();

                    for (int k = 0; k < SecondLevelAry.length(); k++) {
                        JSONObject thirdObj = SecondLevelAry.getJSONObject(k);

                        String thirsLevelName = thirdObj.getString("Name");

                        IconTreeItemHolder.IconTreeItem threeTreeItem1 = new IconTreeItemHolder.IconTreeItem(thirsLevelName);
                        TreeNode levelThreeoneChild0 = new TreeNode(threeTreeItem1);
                        levelThreeoneChild0.setViewHolder(new ThirdLevelHolder(getApplicationContext()));
                        levelTwooneChild0.addChild(levelThreeoneChild0);

                        JSONArray forthAry = thirdObj.getJSONArray(thirsLevelName);
                        for (int l = 0; l < forthAry.length(); l++) {

                            String forthLevelName = forthAry.getString(l);

                            IconTreeItemHolder.IconTreeItem fourTreeItem1 = new IconTreeItemHolder.IconTreeItem(forthLevelName);
                            TreeNode levelFouroneChild0 = new TreeNode(fourTreeItem1);
                            levelFouroneChild0.setViewHolder(new ForthLevelHolder(getApplicationContext()));
                            levelThreeoneChild0.addChild(levelFouroneChild0);


                        }

                    }

                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        LinearLayout container1 = (LinearLayout) view.findViewById(R.id.container);
        AndroidTreeView tView = new AndroidTreeView(getContext(), root);

      /*  tView.setDefaultAnimation(true);
        tView.setUse2dScroll(true);*/
        tView.setDefaultNodeClickListener(TutorGuides.this);
       /* tView.setUseAutoToggle(false);*/
        container1.addView(tView.getView());


        return view;
    }


    @Override
    public void onClick(TreeNode node, Object value) {


//        Toast.makeText(getContext(), "Level :-" + node.getLevel(), Toast.LENGTH_SHORT).show();


        if (node.getLevel() == 1) {
            View view = node.getViewHolder().getView();
            ImageView imageView = (ImageView) view.findViewById(R.id.tutor_guide_img);
            if (node.isExpanded()) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.side_aerrow));
            } else {

                imageView.setImageDrawable(getResources().getDrawable(R.drawable.down_aerrow));
            }
        }

        if (node.getLevel() == 2) {
            View view = node.getViewHolder().getView();
            ImageView imageView = (ImageView) view.findViewById(R.id.tutor_guide_img);
            if (node.isExpanded()) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.plus_png));
            } else {

                imageView.setImageDrawable(getResources().getDrawable(R.drawable.minus_png));
            }
        }

        if (node.getLevel() == 3) {
            View view = node.getViewHolder().getView();
            ImageView imageView = (ImageView) view.findViewById(R.id.tutor_guide_img);
            if (node.isExpanded()) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.plus_png));
            } else {

                imageView.setImageDrawable(getResources().getDrawable(R.drawable.minus_png));
            }
        }



    }


}
