package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    /* как это хранить??? */
    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        timetable = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>());
        }
    }

    //как реализовать, тоже непонятно, но сложность должна быть О(1)
    public SortedMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySessions = timetable.get(dayOfWeek);
        if (daySessions == null) {
            return Collections.emptySortedMap();
        }
        return Collections.unmodifiableSortedMap(daySessions);
    }

    //как реализовать, тоже непонятно, но сложность должна быть О(1)
    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySessions = timetable.get(dayOfWeek);
        List<TrainingSession> sessionsAtTime = daySessions.get(timeOfDay);

        if (sessionsAtTime == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(sessionsAtTime);
    }


    //сохраняем занятие в расписании
    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> daySessions = timetable.get(day);

        List<TrainingSession> sessionsAtTime = daySessions.computeIfAbsent(time, k -> new ArrayList<>());

        sessionsAtTime.add(trainingSession);
    }

    public List<CoachTrainingCount> getCountByCoaches() {
        Map<Coach, Integer> coachCount = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> daySessions : timetable.values()) {
            for (List<TrainingSession> sessionsAtTime : daySessions.values()) {
                for (TrainingSession session : sessionsAtTime) {
                    Coach coach = session.getCoach();
                    coachCount.put(coach, coachCount.getOrDefault(coach, 0) + 1);
                }
            }
        }

        List<CoachTrainingCount> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachCount.entrySet()) {
            result.add(new CoachTrainingCount(entry.getKey(), entry.getValue()));
        }

        result.sort((c1, c2) -> Integer.compare(c2.getCount(), c1.getCount()));
        return result;
    }
}
