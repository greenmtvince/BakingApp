package com.quantrian.bakingapp;

import android.content.Context;

import com.quantrian.bakingapp.data.ContractIngredient;
import com.quantrian.bakingapp.models.Recipe;
import com.quantrian.bakingapp.utils.NetworkUtilities;
import com.quantrian.bakingapp.utils.UrlConfig;

import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Vinnie on 12/30/2017.
 * Tests that the GSON converter works as intended
 */

public class NetworkUtilitiesTest {
    private static final String jsonStuff = "[{\"name\":\"Nutella Pie\",\"id\":1,\"servings\":8,\"image\":\"\",\"ingredients\":[{\"quantity\":2.0,\"measure\":\"CUP\",\"ingredient\":\"Graham Cracker crumbs\"},{\"quantity\":6.0,\"measure\":\"TBLSP\",\"ingredient\":\"unsalted butter, melted\"},{\"quantity\":0.5,\"measure\":\"CUP\",\"ingredient\":\"granulated sugar\"},{\"quantity\":1.5,\"measure\":\"TSP\",\"ingredient\":\"salt\"},{\"quantity\":5.0,\"measure\":\"TBLSP\",\"ingredient\":\"vanilla\"},{\"quantity\":1.0,\"measure\":\"K\",\"ingredient\":\"Nutella or other chocolate-hazelnut spread\"},{\"quantity\":500.0,\"measure\":\"G\",\"ingredient\":\"Mascapone Cheese(room temperature)\"},{\"quantity\":1.0,\"measure\":\"CUP\",\"ingredient\":\"heavy cream(cold)\"},{\"quantity\":4.0,\"measure\":\"OZ\",\"ingredient\":\"cream cheese(softened)\"}],\"steps\":[{\"id\":0,\"shortDescription\":\"Recipe Introduction\",\"description\":\"Recipe Introduction\",\"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4\",\"thumbnailURL\":\"\"},{\"id\":1,\"shortDescription\":\"Starting prep\",\"description\":\"1. Preheat the oven to 350°F. Butter a 9\\\" deep dish pie pan.\",\"videoURL\":\"\",\"thumbnailURL\":\"\"},{\"id\":2,\"shortDescription\":\"Prep the cookie crust.\",\"description\":\"2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.\",\"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9a6_2-mix-sugar-crackers-creampie/2-mix-sugar-crackers-creampie.mp4\",\"thumbnailURL\":\"\"},{\"id\":3,\"shortDescription\":\"Press the crust into baking form.\",\"description\":\"3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature.\",\"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9cb_4-press-crumbs-in-pie-plate-creampie/4-press-crumbs-in-pie-plate-creampie.mp4\",\"thumbnailURL\":\"\"},{\"id\":4,\"shortDescription\":\"Start filling prep\",\"description\":\"4. Beat together the nutella, mascarpone, 1 teaspoon of salt, and 1 tablespoon of vanilla on medium speed in a stand mixer or high speed with a hand mixer until fluffy.\",\"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd97a_1-mix-marscapone-nutella-creampie/1-mix-marscapone-nutella-creampie.mp4\",\"thumbnailURL\":\"\"},{\"id\":5,\"shortDescription\":\"Finish filling prep\",\"description\":\"5. Beat the cream cheese and 50 grams (1/4 cup) of sugar on medium speed in a stand mixer or high speed with a hand mixer for 3 minutes. Decrease the speed to medium-low and gradually add in the cold cream. Add in 2 teaspoons of vanilla and beat until stiff peaks form.\",\"videoURL\":\"\",\"thumbnailURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda20_7-add-cream-mix-creampie/7-add-cream-mix-creampie.mp4\"},{\"id\":6,\"shortDescription\":\"Finishing Steps\",\"description\":\"6. Pour the filling into the prepared crust and smooth the top. Spread the whipped cream over the filling. Refrigerate the pie for at least 2 hours. Then it\\u0027s ready to serve!\",\"videoURL\":\"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda45_9-add-mixed-nutella-to-crust-creampie/9-add-mixed-nutella-to-crust-creampie.mp4\",\"thumbnailURL\":\"\"}]}]";

    /*@Test
    public void GsonConverts() throws Exception {



        //mMockContext.getApplicationContext();
        ArrayList<Recipe> recipes;
        recipes= NetworkUtilities.convertArray(jsonStuff);
        int recipeCount=0;
        int stepCount =0;
        int ingredientCount = 0;
        if (recipes!=null) {
            recipeCount = recipes.size();
            ingredientCount = recipes.get(0).ingredients.size();
            stepCount = recipes.get(0).steps.size();
        }

        assertEquals("There should be 1 recpie in the List", 1, recipeCount );
        assertEquals("There should be 7 steps in the List", 7, stepCount );
        assertEquals("There should be 9 objects in the List", 9, ingredientCount );
    }*/

    @Test
    public void getRemoteString() throws Exception{
        String itemsJson = NetworkUtilities.fetchPlainText(UrlConfig.BASE_URL);

        assertTrue("String Empty",itemsJson.length()>0);
    }

    @Test
    public void getRemoteList() throws Exception{
        ArrayList<Recipe> recipeArrayList = NetworkUtilities.fetchJsonArray();

        assertTrue("ArrayList Empty", recipeArrayList.size()>0);
    }

    /*@Test
    public void stringifyJson() throws Exception{

        ArrayList<Recipe> recipeArrayList = NetworkUtilities.convertArray(jsonStuff);

        if (recipeArrayList!=null){
            assertEquals("There should be 7 steps in the List",7,
                    recipeArrayList.get(0).steps.size());
        }

        String stringyOut = NetworkUtilities.convertToString(recipeArrayList);

        assertEquals("Strings don't match",jsonStuff,stringyOut);

    }*/
}
