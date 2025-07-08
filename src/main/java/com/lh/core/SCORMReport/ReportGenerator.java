package com.lh.core.SCORMReport;

import com.lh.core.SCORMReport.model.ScormData;

public interface ReportGenerator {
    String generate(ScormData data);
}
