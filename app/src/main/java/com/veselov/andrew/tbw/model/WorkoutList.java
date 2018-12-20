package com.veselov.andrew.tbw.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class WorkoutList {
    private static final WorkoutList ourInstance = new WorkoutList();
    private List<Workout> workouts = new ArrayList<>();

    private WorkoutList() {
        initMockList();
    }

    public static WorkoutList getInstance() {
        return ourInstance;
    }

    private void initMockList() {
        workouts.add(new Workout("Строгие подтягивания на перекладине",
                "Повиснуть руками на неподвижной перекладине, плавно без рывков подтяните руками своё тело вверх так, чтобы подбородок оказался выше перекладины. " +
                        "Затем плавно опуститесь вниз, продолжая удерживать руки на перекладине. Корпус держите строго вертикально, без раскачиваний.",
                "https://turnik.su/wp-content/uploads/2017/07/kak-uvelichity-kolichestvo-podtyagivaniy-1024x616.jpg"));

        workouts.add(new Workout("Отжимания от пола",
                "Принять упор лежа на ровной горизонтальной поверхности, руки расположить на ширине плеч, ладони направить вверх, стопы на ширине таза, пальцами " +
                        "ног упираться в пол. На вдохе руки сгибаются в локтях, корпус направляется вниз. Корпус должен образовать ровную линию, и во время наклона эта линия " +
                        "не должна ломаться. На выдохе принимается исходное положение.",
                "http://vremya-sovetov.ru/pic2/o-PUSH-UP-VARIATIONS-facebook633.jpg"));

        workouts.add(new Workout("Бурпи (burpee)",
                "1. Встать прямо, расправив плечи и поставив ноги на их ширину. Сзади вас должно остаться свободное пространство, примерно равное росту.\n" +
                        "2. Присесть на корточки, опершись ладонями о пол. Локти должны находиться в одной плоскости с коленями.\n" +
                        "3. Упираясь выпрямленными руками в пол, рывком перевести тело в горизонтальное положение. Корпус и ноги должны быть выпрямлены в одну линию, как при выполнении планки.\n" +
                        "4. Сделать одно отжимание, полностью распрямив руки.\n" +
                        "5. Рывком вернуться в позицию 2 (присед с упором) и сразу же как можно выше выпрыгнуть из приседа, распрямив корпус и подняв руки вверх. Можно в высшей точке прыжка сделать хлопок ладонями над головой.\n" +
                        "Все эти движения необходимо выполнять непрерывно одно за другим без пауз и без отдыха между повторениями, в быстром темпе.",
                "https://school-body.net/images/statyi/upragnenia-2/burpi-effektivnaya-trenirovka-dlya-vsego-tela3.jpg"));

            // Adding some records for testing RecyclerView
            for (int i = 3; i < 10; i++) {
                workouts.add(new Workout("Упражнение № " + (i + 1),
                        "Описание упражнения № " + (i + 1),
                        "http://timelady.ru/uploads/posts/2016-10/1476312925_i_2b469161c5969e6c_html_ec45a5c0.jpg"));
            }
    }


    public List<Workout> getWorkouts() {
        return workouts;
    }

    public List<Workout> getFavoritsWorkouts() {
            List<Workout> favorits = new ArrayList<>();
            for (Workout l: workouts) if (l.isFavorite()) favorits.add(l);
            return favorits;
    }

    public void addWorkout(String title, String description, String url) {
        workouts.add(new Workout(title,description,url));
    }

    public Workout getWorkoutByIndex(int index) {
        if (index >= workouts.size() || index < 0) return null;
        else return workouts.get(index);
    }
}
