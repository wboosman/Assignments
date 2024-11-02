public class Task {
    private final int taskNumber;
    private final int startTime;
    private final int endTime;
    private final String startPlatform;
    private final String endPlatform;

    public Task(int taskNumber, int startTime, int endTime, String startPlatform, String endPlatform) {
        this.taskNumber = taskNumber;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startPlatform = startPlatform;
        this.endPlatform = endPlatform;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public String getStartPlatform() {
        return startPlatform;
    }

    public String getEndPlatform() {
        return endPlatform;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskNumber=" + taskNumber +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", startPlatform='" + startPlatform + '\'' +
                ", endPlatform='" + endPlatform + '\'' +
                '}';
    }
}
