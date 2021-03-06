package com.example.joeroskopf.resume.db

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.example.joeroskopf.resume.model.network.TacoResponse
import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class FoodDatabaseTest {

    private var tacoDao: TacoDao? = null
    private val gson = Gson()

    @Before
    fun setup() {
         tacoDao = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AppDatabase::class.java).build().tacoDao()
    }

    @Test
    fun insertTacoTest() {
        /**
         * Test to make sure an inserted item doesn't have its ID mutated after inserting
         */
        val sampleTacoResponseFromApi = "{\"mixin\": {\"recipe\": \"#Sauted Shitake Mushroom and Wasabi Salad\\n\\n\\n- 1 cup dried shitake mushrooms \\n- Splash of soy sauce\\n- Splash of red wine vinegar\\n- Splash of olive oil\\n- Wasabi-covered sesame seeds (available at the local Asian mart. If you can't find it, sub just a smidge of wasabi)\\n- Pinch of salt\\n\\nPlace shitakes in a bowl and cover with warm water. Soak for about 20 minutes or until shitakes are completely rehydrated. Remove mushrooms and squeeze excess water from them. They don't need to be completely dry, but shouldn't be sopping wet.\\n\\nHeat olive oil over medium-high heat in a small frying pan. Throw in soy sauce and red wine vinegar. How much depends on your taste. I would start with a tablespoon of each and adjust from there. I like the salad to be a bit more acidic so I tend to add a splash more of the red wine vinegar. Saut\\u00e9 mushrooms for a couple of minutes until they are heated through. Season to taste with salt. Adjust soy sauce and red wine vinegar as needed. Before serving, sprinkle a little bit of the wasabi-covered sesame seeds over the mushrooms to impart a little color, crunchiness, and heat. If you are unable to find wasabi-covered sesame seeds, a dab of wasabi and sprinkle of plain sesame seeds should do the trick. \\n\\nNote: I keep dried shitakes as a staple in my pantry. I love the fact that the water left over after you rehydrate the mushrooms is called mushroom liquor. Mushroom liquor can be used as a substitute for broth or water in many recipes to impart an lovely mushroomy flavor.\", \"url\": \"https://raw.github.com/sinker/tacofancy/master/mixins/mushroom_wasabi_salad.md\", \"name\": \"Mushroom Wasabi Salad\", \"slug\": \"mushroom_wasabi_salad\"}, \"seasoning\": {\"recipe\": \"Packaged Seasonings\\n==============\\n\\nLest we be accused of snobbery, let's acknowledge that a taco is a taco, and taco night can be well-served by not having to think at all.\\n\\nFeel free to use the pre-packaged seasoning of your choice. When I'm in an old-school mood, I head straight for...\\n\\n* Old El Paso Taco Seasoning\\n\\ntags: vegetarian, vegan\\n\", \"url\": \"https://raw.github.com/sinker/tacofancy/master/seasonings/packaged_seasonings.md\", \"name\": \"Packaged Seasonings\", \"slug\": \"packaged_seasonings\"}, \"base_layer\": {\"recipe\": \"onion\\n========\\n\\nAn onion is typically cut up and then tossed into a skillet or something else to heat it. Olive oil is nice. They can be grilled or caramelized as well. \\n\", \"url\": \"https://raw.github.com/sinker/tacofancy/master/base_layers/onion.md\", \"name\": \"onion\", \"slug\": \"onion\"}, \"condiment\": {\"recipe\": \"Vindaloo Sauce\\n================\\n\\nVindaloo sauce can be prepared or store bought.\\nVindaloo is an Indian curry sauce that is popular in the Goa region of India. \\n\\n\", \"url\": \"https://raw.github.com/sinker/tacofancy/master/condiments/vindaloo_sauce.md\", \"name\": \"Vindaloo Sauce\", \"slug\": \"vindaloo_sauce\"}, \"shell\": {\"recipe\": \"Fresh Corn Tortillas\\n===================\\n\\nThis is the only way to go. So worth it. Makes roughly 15 tortillas.\\n\\n* 1 3/4 cups masa harina\\n* 1 1/8 cups water\\n\\n1. In a medium bowl, mix together masa harina and hot water until thoroughly combined. Turn dough onto a clean surface and knead until pliable and smooth. If dough is too sticky, add more masa harina; if it begins to dry out, sprinkle with water. Cover dough tightly with plastic wrap and allow to stand for 30 minutes.\\n2. Preheat a cast iron skillet or griddle to medium-high.\\n3. Divide dough into 15 equal-size balls. Using a tortilla press (or a rolling pin), press each ball of dough flat between two sheets of wax paper (plastic wrap or a freezer bag cut into halves will also work).\\n4. Place tortilla in preheated pan and allow to cook for approximately 30 seconds, or until browned and slightly puffy. Turn tortilla over to brown on second side for approximately 30 seconds more, then transfer to a plate. Repeat process with each ball of dough. Keep tortillas covered with a towel to stay warm and moist (or a low temp oven) until ready to serve.\\n\\ntags: vegetarian, vegan\\n\", \"url\": \"https://raw.github.com/sinker/tacofancy/master/shells/Fresh_corn_tortillas.md\", \"name\": \"Fresh Corn Tortillas\", \"slug\": \"fresh_corn_tortillas\"}}"
        val tacoEntity = gson.fromJson(sampleTacoResponseFromApi, TacoResponse::class.java).toTacoEntity()

        tacoDao?.insertTaco(tacoEntity)
        val maybeTaco = tacoDao?.selectTaco(tacoEntity.id)
        maybeTaco?.subscribe({
            assertEquals(it?.id, tacoEntity.id)
        }, {
            fail("Error subscribing to taco dao")
        })
    }

    @Test
    fun selectAllTest() {
        /**
         * Test to make sure the amount inserted equals the amount we get through a select all
         */

        val tacosToInsert = returnTacos(3)
        for(taco in tacosToInsert) {
            tacoDao?.insertTaco(taco)
        }

        val selectAllLiveData = tacoDao?.selectAll()
        if(selectAllLiveData == null) {
            fail("Didn't get back any results")
        } else {
            val selectAllValue = getValue(selectAllLiveData)
            assertEquals(selectAllValue.size, 3)
        }
    }

    @Test
    fun selectTacoTest() {
        /**
         * Test to verify the we can retrieve a taco by ID
         */
        val tacoToInsert = returnTacos(1)
        for(taco in tacoToInsert) {
            tacoDao?.insertTaco(taco)
        }

        //make sure there is one item in there
        val selectAllLiveData = tacoDao?.selectAll()
        if(selectAllLiveData == null) {
            fail("Didn't get back any results")
        } else {
            val selectAllValue = getValue(selectAllLiveData)
            assertEquals(selectAllValue.size, 1)
        }

        //make sure that one item has the same id as the one we inserted
        val selectTaco = tacoDao?.selectTaco(tacoToInsert[0].id)
        selectTaco?.subscribe({
            assertEquals(tacoToInsert[0].id, it?.id)
        }, {
            fail("Failed to subscribe to taco select")
        })
    }

    @Test
    fun deleteTacoTest() {
        /**
         * Test to verify deleting works
         */
        val tacosToInsert = returnTacos(2)
        //insert 2 tacos
        for(taco in tacosToInsert) {
            tacoDao?.insertTaco(taco)
        }

        //delete the first one
        tacoDao?.deleteTaco(tacosToInsert[0])

        //make sure the one remaining is the one we would expect to be remaining
        val selectTaco = tacoDao?.selectTaco(tacosToInsert[1].id)
        selectTaco?.subscribe({
            assertEquals(tacosToInsert[1].id, it?.id)
        }, {
            fail("Failed to subscribe to taco select")
        })

        //make sure there is only 1 taco remaining
        val selectAllLiveData = tacoDao?.selectAll()
        if(selectAllLiveData == null) {
            fail("Didn't get back any results")
        } else {
            val selectAllValue = getValue(selectAllLiveData)
            assertEquals(selectAllValue.size, 1)
        }
    }

    /**
     * Helper method to return between 1 and 3 taco entities
     */
    private fun returnTacos(howMany: Int): List<TacoEntity> {
        val firstInsert = "{\"mixin\": {\"recipe\": \"#Sauted Shitake Mushroom and Wasabi Salad\\n\\n\\n- 1 cup dried shitake mushrooms \\n- Splash of soy sauce\\n- Splash of red wine vinegar\\n- Splash of olive oil\\n- Wasabi-covered sesame seeds (available at the local Asian mart. If you can't find it, sub just a smidge of wasabi)\\n- Pinch of salt\\n\\nPlace shitakes in a bowl and cover with warm water. Soak for about 20 minutes or until shitakes are completely rehydrated. Remove mushrooms and squeeze excess water from them. They don't need to be completely dry, but shouldn't be sopping wet.\\n\\nHeat olive oil over medium-high heat in a small frying pan. Throw in soy sauce and red wine vinegar. How much depends on your taste. I would start with a tablespoon of each and adjust from there. I like the salad to be a bit more acidic so I tend to add a splash more of the red wine vinegar. Saut\\u00e9 mushrooms for a couple of minutes until they are heated through. Season to taste with salt. Adjust soy sauce and red wine vinegar as needed. Before serving, sprinkle a little bit of the wasabi-covered sesame seeds over the mushrooms to impart a little color, crunchiness, and heat. If you are unable to find wasabi-covered sesame seeds, a dab of wasabi and sprinkle of plain sesame seeds should do the trick. \\n\\nNote: I keep dried shitakes as a staple in my pantry. I love the fact that the water left over after you rehydrate the mushrooms is called mushroom liquor. Mushroom liquor can be used as a substitute for broth or water in many recipes to impart an lovely mushroomy flavor.\", \"url\": \"https://raw.github.com/sinker/tacofancy/master/mixins/mushroom_wasabi_salad.md\", \"name\": \"Mushroom Wasabi Salad\", \"slug\": \"mushroom_wasabi_salad\"}, \"seasoning\": {\"recipe\": \"Packaged Seasonings\\n==============\\n\\nLest we be accused of snobbery, let's acknowledge that a taco is a taco, and taco night can be well-served by not having to think at all.\\n\\nFeel free to use the pre-packaged seasoning of your choice. When I'm in an old-school mood, I head straight for...\\n\\n* Old El Paso Taco Seasoning\\n\\ntags: vegetarian, vegan\\n\", \"url\": \"https://raw.github.com/sinker/tacofancy/master/seasonings/packaged_seasonings.md\", \"name\": \"Packaged Seasonings\", \"slug\": \"packaged_seasonings\"}, \"base_layer\": {\"recipe\": \"onion\\n========\\n\\nAn onion is typically cut up and then tossed into a skillet or something else to heat it. Olive oil is nice. They can be grilled or caramelized as well. \\n\", \"url\": \"https://raw.github.com/sinker/tacofancy/master/base_layers/onion.md\", \"name\": \"onion\", \"slug\": \"onion\"}, \"condiment\": {\"recipe\": \"Vindaloo Sauce\\n================\\n\\nVindaloo sauce can be prepared or store bought.\\nVindaloo is an Indian curry sauce that is popular in the Goa region of India. \\n\\n\", \"url\": \"https://raw.github.com/sinker/tacofancy/master/condiments/vindaloo_sauce.md\", \"name\": \"Vindaloo Sauce\", \"slug\": \"vindaloo_sauce\"}, \"shell\": {\"recipe\": \"Fresh Corn Tortillas\\n===================\\n\\nThis is the only way to go. So worth it. Makes roughly 15 tortillas.\\n\\n* 1 3/4 cups masa harina\\n* 1 1/8 cups water\\n\\n1. In a medium bowl, mix together masa harina and hot water until thoroughly combined. Turn dough onto a clean surface and knead until pliable and smooth. If dough is too sticky, add more masa harina; if it begins to dry out, sprinkle with water. Cover dough tightly with plastic wrap and allow to stand for 30 minutes.\\n2. Preheat a cast iron skillet or griddle to medium-high.\\n3. Divide dough into 15 equal-size balls. Using a tortilla press (or a rolling pin), press each ball of dough flat between two sheets of wax paper (plastic wrap or a freezer bag cut into halves will also work).\\n4. Place tortilla in preheated pan and allow to cook for approximately 30 seconds, or until browned and slightly puffy. Turn tortilla over to brown on second side for approximately 30 seconds more, then transfer to a plate. Repeat process with each ball of dough. Keep tortillas covered with a towel to stay warm and moist (or a low temp oven) until ready to serve.\\n\\ntags: vegetarian, vegan\\n\", \"url\": \"https://raw.github.com/sinker/tacofancy/master/shells/Fresh_corn_tortillas.md\", \"name\": \"Fresh Corn Tortillas\", \"slug\": \"fresh_corn_tortillas\"}}"
        val secondInsert = "{\"mixin\": {\"recipe\": \"Sweet Potato and Apple Hash\\n===========================\\n\\nSweet potatoes are my go-to taco punch-up. I was going my normal route of making small (1/4\\\" or so) cubes of sweet potatoes when I thought: Hey, it's fall, I'm going to add an apple in there. Fuck yes.\\n\\n* 2 Small sweet potatoes (you'd be amazed how little sweet potato you need for tacos)\\n* 1 Small apple\\n* One can diced green chilis\\n* A couple pinches of chili powder\\n* A squeeze of honey\\n* Pat o' butter\\n\\nMix this all into a pan, with about 1/4 cup of water, and boil it all up until the water goes away, toss in the butter, and continue to pan-fry until things get a little browned. \\n\\ntags: vegetarian\\n\", \"url\": \"https://raw.github.com/sinker/tacofancy/master/mixins/sweet_potato_and_apple_hash.md\", \"name\": \"Sweet Potato and Apple Hash\", \"slug\": \"sweet_potato_and_apple_hash\"}, \"seasoning\": {\"recipe\": \"Packaged Seasonings\\n==============\\n\\nLest we be accused of snobbery, let's acknowledge that a taco is a taco, and taco night can be well-served by not having to think at all.\\n\\nFeel free to use the pre-packaged seasoning of your choice. When I'm in an old-school mood, I head straight for...\\n\\n* Old El Paso Taco Seasoning\\n\\ntags: vegetarian, vegan\\n\", \"url\": \"https://raw.github.com/sinker/tacofancy/master/seasonings/packaged_seasonings.md\", \"name\": \"Packaged Seasonings\", \"slug\": \"packaged_seasonings\"}, \"base_layer\": {\"recipe\": \"Baja Beer Battered Fish\\n========================\\n\\nThis is the beer battered fish for [Baja fish tacos](../full_tacos/baja_fish_tacos.md).\\n\\nBatter\\n-------\\n\\n* 1/2 c flour\\n* 1/4 c baking powder\\n* 1 egg\\n* 1/2 c beer (Tecate!)\\n* 1 tsp salt\\n* 1 tsp chili powder (paprika, salt, garlic salt)\\n* 2 tsp parsley\\n\\nBeat the eggs and fold all batter ingredients until smooth.\\nAdd flour to make it thicker or add beer to make it thinner.\\nIf you have time let the batter chill for a couple hours.\\n\\nFish\\n------\\n\\n* 1 lb. cod fillet, grouper, or similar white fish, the fresher the better\\n\\n* Cut fish into small chunks.\\n* Fill a pan with oil and heat until approx. 375 F.\\n* Fill a bowl with about a cup of flour and keep it next to the batter bowl.\\n* Dip a fish piece into flour then into the batter.\\n* Fry for 4-5 minutes, turning halfway through. Drain on a paper towel.\", \"url\": \"https://raw.github.com/sinker/tacofancy/master/base_layers/baja_beer_batter.md\", \"name\": \"Baja Beer Battered Fish\", \"slug\": \"baja_beer_battered_fish\"}, \"condiment\": {\"recipe\": \"# Above Average Tomato Sauce\\n\\n## Ingredients\\n\\n* 1 24oz jar or can of crushed tomatos or tomato puree\\n* 1 T olive oil\\n* 1/4 to 1/2 of a medium onion\\n* 2 cloves garlic\\n* 1 red bell pepper\\n* 1 t Italian seasoning\\n* salt to taste\\n\\n## Instructions\\n\\n1. Chop onion, garlic and pepper.\\n2. Heat olive oil in a skillet and sautee the onion and garlic until the onion is translucent.\\n3. Add salt, Italian seasoning and bell pepper and sautee until the pepper begins to soften.\\n4. Add crushed tomatos and simmer while the rest of your meal cooks.\\n\\ntags: vegan, vegetarian\\n\", \"url\": \"https://raw.github.com/sinker/tacofancy/master/condiments/tomato_sauce_above_average.md\", \"name\": \"Above Average Tomato Sauce\", \"slug\": \"above_average_tomato_sauce\"}, \"shell\": {\"recipe\": \"bad-ass tortillas\\n=====================\\n\\nIf you are making tacos, don't settle for corporate store bought tortillas. Make your own like a real person!\\n\\nFirst get a cast iron pan:\\n\\n![](./pan.jpg)\\n\\nand then one of these bad-ass tortilla presses:\\n\\n![](./tortillador.jpg)\\n\\nBuy your lard from a place like this:\\n\\n![](./store.jpg)\\n\\n* 2 cups all purpose flour\\n* 1/4 cup lard (cut into lil' pieces)\\n* 1 teaspoon kosher salt\\n* 2/3 tablespoon oil\\n* 1/2 cup water (luke warm)\\n\\nMix all ingredients together except oil and water. Drizzle oil over mixture and mix with hands. Add water and mix and knead again until doughy. Let chill for about an hour in plastic wrappers.\\n\\nHeat large cast iron skillet (or something more authentic if you've got it) over medium heat. Cut dough into about 12 pieces that are round. Use a proper tortilla press (or something more authentic if you've got it) to make 'em flat and then put on the skillet. Wait until the transparent parts turn opaque and flip em. Put cooked tortillas in a *dirty* cloth napkin to keep 'em warm. End recipe. Paz, amor, y dinero.\\n\", \"url\": \"https://raw.github.com/sinker/tacofancy/master/shells/homemade_tortillas.md\", \"name\": \"bad-ass tortillas\", \"slug\": \"bad_ass_tortillas\"}}"
        val thirdInsert = "{\"mixin\": {\"recipe\": \"Veggies for Fish Tacos\\n======================\\n\\nFish tacos are a special breed, requiring different vegetable options.\\n\\n__Assemble your veg from the following options:__\\n\\n* Cabbage, purple, shredded\\n* Cabbage, other shades, shredded\\n* Radishes, sliced into thin slices\\n* Red peppers, diced\\n* Cherry tomatoes, sliced (if you're a heathen)\\n* Cilantro, if it doesn't taste like soap to you\\n\\nAnd one requirement:\\n* Limes, sliced for juicing over tacos.\\n\\nPlace out your selections and assemble into your taco. Then squeeze a lime over the top.\\n\\ntags: vegetarian, vegan\\n\", \"url\": \"https://raw.github.com/sinker/tacofancy/master/mixins/veg_for_fish_tacos.md\", \"name\": \"Veggies for Fish Tacos\", \"slug\": \"veggies_for_fish_tacos\"}, \"seasoning\": {\"recipe\": \"Universal Taco Seasoning\\n========================\\n\\nI got tired of buying packets of store-bought taco seasoning, so I experimented with various spices and ratios until I landed on this recipe. I keep a jar of it in the cupboard at all times.\\n\\n* 6 tbsp chili powder\\n* 4 tbsp cumin\\n* 4 tbsp corn starch\\n* 3 tbsp onion powder\\n* 1 tbsp salt\\n* 1 tbsp garlic powder\\n* 4 tsp oregano (Mexican oregano, if you've got it)\\n* 2 tsp crushed red pepper\\n\\nCombine in Mason jar and shake well to combine.\\n\\nThis mix works well for chicken, pork and beef, destined for the grill, oven, slow cooker or stovetop. You could tweak it a bit to target a specific meat, but I like to have a base, universal mix around. Makes it super easy to turn leftover anything into delicious taco filling: Just chop up whatever it is, toss it into a skillet, sprinkle generously with seasoning, then add a bit of water and simmer to impart flavor.\\n\\ntags: vegetarian, vegan\\n\", \"url\": \"https://raw.github.com/sinker/tacofancy/master/seasonings/universal_taco_seasoning.md\", \"name\": \"Universal Taco Seasoning\", \"slug\": \"universal_taco_seasoning\"}, \"base_layer\": {\"recipe\": \"@deezthugs' Smokey Turkey Tacos\\n===============================\\n\\nThese tacos have and will blow minds.\\n\\n(Note:  I cannot separate the base_layer here from the seasoning, it is all integral)\\n\\n* 2 Packages ground Turkey or Chicken (1.5 to 2 lbs) - not the lean stuff for Chrissake!\\n* Several slices of cooked bacon, diced\\n* 2 Tbs Coconut oil\\n* 2 Tbs Bacon Grease. That's right, Bacon Grease. (What you don't keep it? Might as well just quit now)\\n* 1 Medium sweet onion\\n* (The following dry ingrediants can be increased depending on amount of meat)  \\n* 4 Garlic Cloves, smashed\\n* 1 tsp Cumin\\n* 2 tsp Onion powder\\n* 1 tsp Chipotle powder (use as much as required)\\n* 1 tsp (cool smokey) Paprika\\n* 1 tsp Cinnamon\\n* 1 tsp Ground Ginger\\n* 1/2 - 1 tsp Black Pepper\\n* 1 tsp Kosher Salt\\n* 3 Limes, juiced\\n* [optional] 1/2 can low-salt Chicken broth\\n\\nCombine all dry seasonings in bag, shake up and combine well with raw turkey meat. return to fridge for an hour or more. \\n\\nMelt the Bacon Grease over medium heat, add Coconut oil and diced onion, cook down till onions are, well, you know, awesome. Add the smashed garlic. Add the bacon. Add the turkey meat and break it down into medium/small bits with a spatula or butter knife. After cooking for a few minutes and the meat has begun to brown, drizzle 2 of limes' juice onto the meat.\\n\\nCook well, adding optional chicken broth to shape the consistency as needed. (Generally not needed unless you accidentally buy low-fat meat. Shame on you.)\\n\\nFinally, transfer amount for a meal to a frying pan, frying for a few minutes to create some crispy bits (This is the key step:)), adding lime juice as it cooks.  Spoon onto favorite tortillas add condiments and devour.\\n\", \"url\": \"https://raw.github.com/sinker/tacofancy/master/base_layers/smokey_turkey.md\", \"name\": \"@deezthugs' Smokey Turkey Tacos\", \"slug\": \"deezthugs_smokey_turkey_tacos\"}, \"condiment\": {\"recipe\": \"# Above Average Tomato Sauce\\n\\n## Ingredients\\n\\n* 1 24oz jar or can of crushed tomatos or tomato puree\\n* 1 T olive oil\\n* 1/4 to 1/2 of a medium onion\\n* 2 cloves garlic\\n* 1 red bell pepper\\n* 1 t Italian seasoning\\n* salt to taste\\n\\n## Instructions\\n\\n1. Chop onion, garlic and pepper.\\n2. Heat olive oil in a skillet and sautee the onion and garlic until the onion is translucent.\\n3. Add salt, Italian seasoning and bell pepper and sautee until the pepper begins to soften.\\n4. Add crushed tomatos and simmer while the rest of your meal cooks.\\n\\ntags: vegan, vegetarian\\n\", \"url\": \"https://raw.github.com/sinker/tacofancy/master/condiments/tomato_sauce_above_average.md\", \"name\": \"Above Average Tomato Sauce\", \"slug\": \"above_average_tomato_sauce\"}, \"shell\": {\"recipe\": \"bad-ass tortillas\\n=====================\\n\\nIf you are making tacos, don't settle for corporate store bought tortillas. Make your own like a real person!\\n\\nFirst get a cast iron pan:\\n\\n![](./pan.jpg)\\n\\nand then one of these bad-ass tortilla presses:\\n\\n![](./tortillador.jpg)\\n\\nBuy your lard from a place like this:\\n\\n![](./store.jpg)\\n\\n* 2 cups all purpose flour\\n* 1/4 cup lard (cut into lil' pieces)\\n* 1 teaspoon kosher salt\\n* 2/3 tablespoon oil\\n* 1/2 cup water (luke warm)\\n\\nMix all ingredients together except oil and water. Drizzle oil over mixture and mix with hands. Add water and mix and knead again until doughy. Let chill for about an hour in plastic wrappers.\\n\\nHeat large cast iron skillet (or something more authentic if you've got it) over medium heat. Cut dough into about 12 pieces that are round. Use a proper tortilla press (or something more authentic if you've got it) to make 'em flat and then put on the skillet. Wait until the transparent parts turn opaque and flip em. Put cooked tortillas in a *dirty* cloth napkin to keep 'em warm. End recipe. Paz, amor, y dinero.\\n\", \"url\": \"https://raw.github.com/sinker/tacofancy/master/shells/homemade_tortillas.md\", \"name\": \"bad-ass tortillas\", \"slug\": \"bad_ass_tortillas\"}}"

        val firstTacoEntity = gson.fromJson(firstInsert, TacoResponse::class.java).toTacoEntity()
        val secondTacoEntity = gson.fromJson(secondInsert, TacoResponse::class.java).toTacoEntity()
        val thirdTacoEntity = gson.fromJson(thirdInsert, TacoResponse::class.java).toTacoEntity()

        when(howMany){
            0 -> return listOf()
            1 -> return listOf(firstTacoEntity)
            2 -> return listOf(firstTacoEntity, secondTacoEntity)
            3 -> return listOf(firstTacoEntity, secondTacoEntity, thirdTacoEntity)
            else -> fail("You can't as for that many!")
        }

        return listOf()
    }

    // Copied from stackoverflow
    @Throws(InterruptedException::class)
    fun <T> getValue(liveData: LiveData<T>): T {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(t: T?) {
                data[0] = t
                latch.countDown()
                liveData.removeObserver(this)//To change body of created functions use File | Settings | File Templates.
            }

        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)

        return data[0] as T
    }

}