package org.example.controller;

import org.apache.commons.lang3.tuple.Pair;
import org.example.model.EmployeeProject;
import org.example.service.EmployeePairFinder;
import org.example.utility.CSVReader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class FileUploadController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/upload-file")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        try {
            List<EmployeeProject> projects = CSVReader.readCSV(file.getInputStream());
            Map<Pair<Integer, Integer>, List<EmployeeProject>> commonProjects = EmployeePairFinder.findCommonProjects(projects);

            // Create a list to hold the information for the result view
            List<ProjectDetails> projectDetailsList = new ArrayList<>();
            for (Map.Entry<Pair<Integer, Integer>, List<EmployeeProject>> entry : commonProjects.entrySet()) {
                int totalDaysWorked = 0;
                for (EmployeeProject project : entry.getValue()) {
                    long daysWorked = ChronoUnit.DAYS.between(project.getDateFrom(), project.getDateTo());
                    totalDaysWorked += daysWorked;
                }
                projectDetailsList.add(new ProjectDetails(entry.getKey().getLeft(), entry.getKey().getRight(), totalDaysWorked));
            }

            model.addAttribute("projectDetails", projectDetailsList);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error processing file!");
        }
        return "result";
    }

    // Inner class to represent project details
    public static class ProjectDetails {
        private int empId1;
        private int empId2;
        private int totalDaysWorked;

        public ProjectDetails(int empId1, int empId2, int totalDaysWorked) {
            this.empId1 = empId1;
            this.empId2 = empId2;
            this.totalDaysWorked = totalDaysWorked;
        }

        public int getEmpId1() {
            return empId1;
        }

        public int getEmpId2() {
            return empId2;
        }

        public int getTotalDaysWorked() {
            return totalDaysWorked;
        }
    }
}
