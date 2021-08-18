package com.example.driverapplication;

import com.example.driverapplication.Model.JobSolution;

public interface FetchSolutionCallBackInterfaceButWithJobSolution {
    void onError(int message);

    void onPostExecute(JobSolution jobSolution);
}
