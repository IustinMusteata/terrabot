package robot;
import entities.Entity;

import java.util.ArrayList;
import java.util.List;

public final class Terrabot {
    private int x;
    private int y;
    private int batteryLvl;
    private final int maxBatteryLvl;
    private List<Entity> inventory;
    private List<Topic> knowledgeBase;

    public Terrabot(final int x, final int y, final int maxBatteryLvl) {
        this.x = x;
        this.y = y;
        this.maxBatteryLvl = maxBatteryLvl;
        this.batteryLvl = maxBatteryLvl;
        this.inventory = new ArrayList<>();
        this.knowledgeBase = new ArrayList<>();
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public void setX(final int x) {
        this.x = x;
    }
    public void setY(final int y) {
        this.y = y;
    }
    public int getBatteryLvl() {
        return batteryLvl;
    }

    /**
     * Consumes the battery, by removing from its level the percentage given.
     * @param percentage the percentage that is to be removed from the batery level.
     */
    public void consumeBattery(final int percentage) {
        this.batteryLvl -= percentage;
    }

    /**
     * Recharges the battery, adding to its level the percentage received.
     * @param percentage How much the battery will charge.
     */
    public void rechargeBattery(final int percentage) {
        this.batteryLvl += percentage;
    }

    /**
     * Adds the entity to the robot's inventory, and if the entity is not in the knowledge base,
     * a new Topic is created and added in the knowledge base.
     * @param entity the entity that is to be added to the inventory.
     */
    public void addInventory(final Entity entity) {
        this.inventory.add(entity);
        boolean topicIsInKnowledgeBase = false;
        for (Topic topic : this.knowledgeBase) {
            if (topic.getTopic().equals(entity.getName())) {
                topicIsInKnowledgeBase = true;
                break;
            }
        }
        if (!topicIsInKnowledgeBase) {
            this.knowledgeBase.add(new Topic(entity.getName()));
        }
    }

    /**
     * Checks if the inventory has an entity that has the name specified.
     * @param name the name of the entity to be searched in inventory
     * @return true if an entity with the given name exists in inventory,
     * false otherwise
     */
    public boolean hasInInventory(final String name) {
        for (Entity entity : this.inventory) {
            if (entity.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes from the inventory first entity found with the given name, if FOUND.
     * @param name the name of the entity that is to be removed
     */
    public void removefromInventory(final String name) {
        for (int i = 0; i < this.inventory.size(); i++) {
            if (this.inventory.get(i).getName().equals(name)) {
                this.inventory.remove(i);
                return;
            }
        }
    }

    /**
     * Adds given fact to given topic in the knowledgeBase
     * If topic is not in knowledgebase, new Topic is instantiated, fact is added to it, and
     * added to knowledgebase
     * If topic exists and fact already present, it is not added again.
     * @param topicName the name of the topic that fact belongs to
     * @param fact the fact that is to be added to topic
     */
    public void addFact(final String topicName, final String fact) {
        for (Topic topic : this.knowledgeBase) {
            if (topic.getTopic().equals(topicName)) {
                if (!topic.getFacts().contains(fact)) {
                    topic.getFacts().add(fact);
                }
                return;
            }
        }
        Topic newTopic = new Topic(topicName);
        newTopic.getFacts().add(fact);
        this.knowledgeBase.add(newTopic);
    }

    /**
     * Check if given fact that is in given topic is in the knowledge base.
     * @param topicName the name of topic under which the fact should be
     * @param fact the fact that is to searched
     * @return true if topic exists and contains fact, false otherwise
     */
    public boolean hasFact(final String topicName, final String fact) {
        for (Topic t : knowledgeBase) {
            if (t.getTopic().equals(topicName)) {
                for (String f : t.getFacts()) {
                    if (f.contains(fact)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns knowledgebase as a list of Topics.
     * @return returns Knowledgebase.
     */
    public List<Topic> getKnowledgeBase() {
        return knowledgeBase;
    }

    /**
     * Check if Inventory contains given Entity
     * @param entity Entity that is to be searched in inventory
     * @return true if inventory contains entity, false otherwise
     */
    public boolean containsEntity(final Entity entity) {
        return this.inventory.contains(entity);
    }
}

