package com.example.peer360.review.entity;

public enum ReviewItem {
    KNOWLEDGE("업무에 필요한 지식이 풍부한가?"),
    PERFORMANCE("자신의 능력을 바탕으로 주어진 업무를 잘 수행하는가?"),
    PLANNING("업무 전반적인 진행 상황을 이해하고, 계획에 따라 실천하는 능력이 탁월한가?"),
    CREATIVITY("개인의 창의성을 토대로, 업무의 효율 및 효과를 극대화하는 능력이 탁월한가?"),
    ADAPTABILITY("주어진 환경의 변화에 대해 대처 능력이 뛰어난가?"),
    EFFICIENCY("타인과의 업무 진행이 효율적인가?"),
    RELATIONSHIP("타인과의 원만한 관계를 이끌어내는가?"),
    RESPONSIBILITY("주어진 업무에 대해 책임감을 갖고 있는가?"),
    LEADERSHIP("리더십이 있나요?"),
    MORALITY("도덕성이 있나요?");

    private final String description;

    ReviewItem(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static boolean isValid(String description) {
        for (ReviewItem item : values()) {
            if (item.description.equals(description)) {
                return true;
            }
        }
        return false;
    }
}