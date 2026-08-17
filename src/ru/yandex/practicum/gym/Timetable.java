package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    /* как это хранить??? */
    public Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        timetable = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>());
        }
    }

    //как реализовать, тоже непонятно, но сложность должна быть О(1)
    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        List<TrainingSession> result = new ArrayList<>();
        TreeMap<TimeOfDay, List<TrainingSession>> daySessions = timetable.get(dayOfWeek);

        for (List<TrainingSession> sessionsAtTime : daySessions.values()) {
            result.addAll(sessionsAtTime);
        }

        return result;
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

    public List<Map.Entry<Coach, Integer>> getCountByCoaches() {
        Map<Coach, Integer> coachCount = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> daySessions : timetable.values()) {
            for (List<TrainingSession> sessionsAtTime : daySessions.values()) {
                for (TrainingSession session : sessionsAtTime) {
                    Coach coach = session.getCoach();
                    coachCount.put(coach, coachCount.getOrDefault(coach, 0) + 1);
                }
            }
        }

        List<Map.Entry<Coach, Integer>> sortedCoaches = new ArrayList<>(coachCount.entrySet());
        sortedCoaches.sort((entry1, entry2)
                -> entry2.getValue().compareTo(entry1.getValue()));

        return sortedCoaches;
    }
}
