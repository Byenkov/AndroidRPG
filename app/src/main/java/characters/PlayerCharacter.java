package characters;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by asus on 06.06.2017.
 */

@Root
public class PlayerCharacter {
    @Element
    private String name;
    @Element
    private String level;
    @Element
    private SkillLevel strength;
    @Element
    private SkillLevel agility;
    @Element
    private SkillLevel wisdom;
    @Element
    private String portrait;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public SkillLevel getStrength() {
        return strength;
    }

    public void setStrength(SkillLevel strength) {
        this.strength = strength;
    }

    public SkillLevel getAgility() {
        return agility;
    }

    public void setAgility(SkillLevel agility) {
        this.agility = agility;
    }

    public SkillLevel getWisdom() {
        return wisdom;
    }

    public void setWisdom(SkillLevel wisdom) {
        this.wisdom = wisdom;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}