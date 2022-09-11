package me.conclure.concluresdinomod.util;

import net.minecraft.nbt.CompoundTag;

import java.util.function.IntUnaryOperator;

public class StatsTracker {
    private final int max;
    private int attack, stamina, speed, defense, health;

    public StatsTracker(int max) {
        this.max = max;
    }

    public static StatsTracker create(int max) {
        return new StatsTracker(max);
    }

    public static StatsTracker createRandomized(int max, IntUnaryOperator random) {
        StatsTracker tracker = StatsTracker.create(max);
        tracker.randomize(random);
        return tracker;
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        this.setStamina(compound.getInt("Stamina"));
        this.setSpeed(compound.getInt("Speed"));
        this.setDefense(compound.getInt("Defense"));
        this.setAttack(compound.getInt("Attack"));
        this.setHealth(compound.getInt("Health"));
    }

    public CompoundTag saveAdditionalSaveData() {
        CompoundTag compound = new CompoundTag();
        compound.putInt("Attack", this.getAttack());
        compound.putInt("Speed", this.getSpeed());
        compound.putInt("Defense", this.getDefense());
        compound.putInt("Stamina", this.getStamina());
        compound.putInt("Health", this.getHealth());
        return compound;
    }

    public void randomize(IntUnaryOperator random) {
        int bound = this.max + 1;
        this.setSpeed(random.applyAsInt(bound));
        this.setAttack(random.applyAsInt(bound));
        this.setDefense(random.applyAsInt(bound));
        this.setStamina(random.applyAsInt(bound));
        this.setHealth(random.applyAsInt(bound));
    }

    public void setAttack(int attack) {
        this.attack = Math.min(attack, this.max);
    }

    public void setStamina(int stamina) {
        this.stamina = Math.min(stamina,this.max);
    }

    public void setSpeed(int speed) {
        this.speed = Math.min(speed,this.max);
    }

    public void setDefense(int defense) {
        this.defense = Math.min(defense,this.max);
    }

    public void setHealth(int health) {
        this.health = Math.min(health,this.max);
    }

    public double getAttackPercentage() {
        double value = this.attack;
        double max = this.max;
        return value / max;
    }

    public double getStaminaPercentage() {
        double value = this.stamina;
        double max = this.max;
        return value / max;
    }

    public double getSpeedPercentage() {
        double value = this.speed;
        double max = this.max;
        return value / max;
    }

    public double getDefensePercentage() {
        double value = this.defense;
        double max = this.max;
        return value / max;
    }
    public double getHealthPercentage() {
        double value = this.health;
        double max = this.max;
        return value / max;
    }

    public int getAttack() {
        return this.attack;
    }

    public int getStamina() {
        return this.stamina;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getDefense() {
        return this.defense;
    }

    public int getHealth() {
        return this.health;
    }

    public int sum() {
        return this.attack + this.stamina + this.stamina + this.defense + this.health;
    }
}
