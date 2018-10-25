package com.example.base;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.orangesignal.csv.CsvConfig;

public interface BaseExec2 {
	Boolean execDetails(List<Map<String, String>> csvLines,String Dir,String fileName, CsvConfig conf);
}
