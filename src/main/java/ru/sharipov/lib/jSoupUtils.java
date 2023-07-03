package ru.sharipov.lib;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import ru.sharipov.entity.Predictor;

import java.io.IOException;

public class jSoupUtils {

    /**
     * �������� ������������ ������ �������, true = ������ ���������� ���������� �����
     *
     * @param predictor - ��������� �������
     * @return - true, ���� ������ ������ ���������� Title
     */
    public static boolean isCorrectService(Predictor predictor) {
        String url = predictor.getHost();

        Element response = null;
        try {
            response = Jsoup.connect(url).get().head();
        } catch (IOException ignored) {
        }
        if (response == null) {
            return false;
        }
        return response.getElementsByTag("Title").stream()
                .anyMatch(t -> t.toString().contains(predictor.getCheckData()));
    }

}
