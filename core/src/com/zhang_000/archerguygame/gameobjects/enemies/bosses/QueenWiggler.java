package com.zhang_000.archerguygame.gameobjects.enemies.bosses;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.zhang_000.archerguygame.gameobjects.Player;
import com.zhang_000.archerguygame.gameobjects.weapons.Weapon;
import com.zhang_000.archerguygame.gameworld.GameWorld;
import com.zhang_000.archerguygame.helper_classes.AssetLoader;
import com.zhang_000.archerguygame.screens.GameScreen;

public class QueenWiggler extends Boss {

    public static final int SCORE = 20;
    private static final int VELOCITY_UP = -10;
    private static final int VELOCITY_DOWN = 10;
    private final int GROUND_Y;
    private final int ENTERED_X;

    private PositionState positionState;
    private EnergyBall energyBall;

    //Reference to the player needed to calculate direction that new energy ball should head in
    private Player player;

    private enum PositionState {
        ENTERING, ENTERED
    }

    public QueenWiggler(Player player) {
        this.player = player;

        HP = 10;
        width = 40;
        height = 33;
        positionState = PositionState.ENTERING;

        position = new Vector2(GameScreen.GAME_WIDTH, 30);
        velocity = GameWorld.LATERAL_MOVE_SPEED.cpy();
        acceleration = GameWorld.NO_ACCELERATION;

        hitPolygon.setPosition(position.x, position.y);
        hitPolygon.setOrigin(0, 0);
        float[] vertices = {0, 0, width, 0, width, height, 0, height};
        hitPolygon.setVertices(vertices);

        GROUND_Y = GameWorld.GROUND_LEVEL - height;
        ENTERED_X = (int) GameScreen.GAME_WIDTH - width - 5;
    }

    @Override
    public void update(float delta) {
        //If not entirely on screen yet (recently spawned)
        if (position.x > ENTERED_X) {
            deltaPos = velocity.cpy().scl(delta);
            position.add(deltaPos);
        } else if (positionState == PositionState.ENTERING) { //If the Queen Wiggler has JUST entered the screen fully
            positionState = PositionState.ENTERED;
            velocity.set(0, QueenWiggler.VELOCITY_UP);
        }

        //If the Queen Wiggler has fully entered the screen, move her up and down at a constant speed
        if (positionState == PositionState.ENTERED) {
            deltaPos = velocity.cpy().scl(delta);
            position.add(deltaPos);

            //If top of screen has been reached, start going down
            if (position.y < 0) {
                position.set(position.x, 0);
                velocity.set(0, QueenWiggler.VELOCITY_DOWN);
            } else if (position.y > GROUND_Y) {
                position.y = GROUND_Y;
                velocity.set(0, VELOCITY_UP);
            }
        }

        //Update the hit polygon
        hitPolygon.setPosition(position.x, position.y);

        //UPDATE ENERGY BALL
        if (energyBall == null) {
            float dy = player.getY() - position.y;
            float dx = player.getX() - position.x;
            float angle = MathUtils.atan2(dy, dx);
            float vel_x = EnergyBall.VELOCITY_MAGNITUDE * MathUtils.cos(angle);
            float vel_y = EnergyBall.VELOCITY_MAGNITUDE * MathUtils.sin(angle);

            energyBall = new EnergyBall(new Vector2(position.x, position.y), new Vector2(vel_x, vel_y),
                    GameWorld.NO_ACCELERATION);

            AssetLoader.playSound(AssetLoader.soundEnergyBall, 1);
        }

        energyBall.update(delta);

        //Remove the energy ball if it is off the screen or hits the ground
        if (energyBall.getX() < -EnergyBall.WIDTH || energyBall.getY() > GameWorld.GROUND_LEVEL - EnergyBall.HEIGHT) {
            energyBall = null;
        }

    }

    @Override
    public void render(float runTime, SpriteBatch batch) {
        batch.draw(AssetLoader.animationWigQueen.getKeyFrame(runTime), position.x, position.y,
                AssetLoader.animationWigQueen.getKeyFrame(runTime).getRegionWidth(), height);

        if (energyBall != null) {
            energyBall.render(runTime, batch);
        }
    }

    @Override
    public Weapon getWeapon() {
        return energyBall;
    }

    @Override
    public void removeWeapon() {
        energyBall = null;
    }

    public class EnergyBall extends Weapon {

        public static final int WIDTH = 25;
        public static final int HEIGHT = 24;
        public static final int VELOCITY_MAGNITUDE = 100;

        public EnergyBall(Vector2 position, Vector2 velocity, Vector2 acceleration) {
            super(position, velocity, acceleration);

            hitPolygon.setPosition(position.x, position.y);
            hitPolygon.setOrigin(0, 0);
            float[] vertices = {8, 0, 16, 0, 20, 2, WIDTH, 8, WIDTH, 15, 21, 21, 16, HEIGHT, 8, HEIGHT,
                    3, 20, 0, 15, 0, 9, 3, 3};
            hitPolygon.setVertices(vertices);
        }

        @Override
        public void update(float delta) {
            //NO ACCELERATION

            deltaPos = velocity.cpy().scl(delta);
            position.add(deltaPos);

            hitPolygon.setPosition(position.x, position.y);
        }

        @Override
        public void render(float runTime, SpriteBatch batch) {
            batch.draw(AssetLoader.animationEnergyBall.getKeyFrame(runTime), position.x, position.y);
        }

        @Override
        public Polygon getHitPolygon() {
            return hitPolygon;
        }

    }

}
