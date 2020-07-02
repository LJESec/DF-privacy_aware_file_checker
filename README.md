# Privacy_aware_file_checker_for_DF (Web-Service Beta)

This project is a prototypical implementation of an algorithm to perform a privacy check of recovered files during a Digital Forensic Investigation. Since existing approach (e.g.: whitelisting of specific filetypes and reducing the investigation image) can reach their limits the following approach goes into the file-structure of recovered files and decide if they can used for a further investigation.

**Important: This approach requires to have a previously defined set of un-critical data and is focuses on Digital Forensic Investigation in Enterprises.**

*This prototype is under continuous development and should not yet be deployed in a productive environment.*

## Concept

The main concept of this approach is the entropy-based file comparison process. The following figure illustrates the comparison of recovered file with a previously acquired reference file:

![Application of the cosine similarity measure](/images/img_fig_2.png)

## Installation

* First install git, python, dos2unix and java
```
 sudo apt-get install -y git
 sudo apt-get install -y python-minimal
 sudo apt-get install -y dos2unix
 sudo apt-get install -y default-jdk
```

* Clone comparison engine
```
 mkdir ~/df-privacy-checker_tools/
 cd ~/df-privacy-checker_tools/
 git clone https://github.com/LudwigEnglbrecht/DF-privacy-checker 
 dos2unix integration_pipeline.sh
```

* Clone this project (REST Web-Service)
```
cd ~/df-privacy-checker_tools/
git clone https://github.com/LudwigEnglbrecht/DF-privacy_aware_file_checker
```






* start the Spring Boot Web-Server (IntelliJ IDEA)

* use the API (e.g. by using Postman)

The web-service provides the following features:
![Overview of the developed interfaces](/images/img_tab1.png)

## Usage

All interaction with the tool is performed via the REST interface. You can use postman to quiery the API:

![Usage](/images/usage1.png)


A logical consistency check of the file comparison process can be triggered by the following steps:


* After the installation move your reference files (e.g.: Business emails containing offers, order confirmations or invoices) into the folder “~/df-privacy-checker_tools/DF-privacy-checker/”

* Move your recovered files for testing into the folder “~/df-privacy-checker_tools/DF-privacy-checker/test_data/”

* Perform the following commands:

```
cd ~/df-privacy-checker_tools/DF-privacy-checker/

# Prepare CSV files wherein each line represents the local entropy per 32 Bit blocks
./step_01_ent-loop.sh 

# Since the files can have different lengths the following script normalizes the values
# The python script also performs a cosine similarity measurement
python step_02_transform_and_compare.py
```

As a result a heatmap indicating the similarity of the provided data (from folder “test_data/”) is retrievable via the following Url: http://127.0.0.1:8000/heatmap_file_comparison_d3.html

![ File comparison heatmap](/images/img_heat1.png)


## Technical details

The following class diagram shows the structure of the prototype:

![Class diagram of the prototype](/images/classdiagram.png)
