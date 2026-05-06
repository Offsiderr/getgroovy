package seng201.team67.models.items;

public class ConditionalItem extends EquippedItem {

    //Conditional items are context sensitive. eg. you get a bonus if you end a concert with 2 artist's combined stamina over 150

    public ConditionalItem(int cost) {
        super(cost);
    }

    //Getters
    @Override
    public double getCost() {
        return 0;
    }


}
