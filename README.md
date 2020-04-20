# Privacy_aware_file_checker_for_DF (beta)

This project is a prototypical implementation of an algorithm to perform a privacy check of recovered files during a Digital Forensic Investigation. Since existing approach (e.g.: whitelisting of specific filetypes and reducing the investigation image) can reach their limits the following approach goes into the file-structure of recovered files and decide if they can used for a further investigation.
Important: This approach requires to have a previously defined set of un-critical data and is focuses on Digital Forensic Investigation in Enterprises.

## Concept

The main concept of this approach is the entropy-based file comparison process. The following figure illustrates the comparison of recovered file with a previously acquired reference file:

![Application of the cosine similarity measure](/images/img_fig_2.png)

## Installation

* First install git, python and java
```
 mkdir ~/df-privacy-checker_tools/
 cd ~/df-privacy-checker_tools/
 git clone https://github.com/LJESec/DF-privacy-checker 
```

* Clone this project
```
cd ~/df-privacy-checker_tools/
git clone https://github.com/LJESec/DF-privacy_aware_file_checker
```

* start the Spring Boot Web-Server (IntelliJ IDEA)

* use the API (e.g. by using Postman)

The web-service provides the following features:
![Overview of the developed interfaces](/images/img_tab1.png)

## Usage

All interaction with the tool is performed via the REST interface. You can use postman to quiery the API:

![Usage](/images/usage1.png)

![ File comparison heatmap](/images/img_heat1.png)


A logical consistency check of the file comparison process can be triggered by the following steps.

As a result a heatmap indicating the similarity of the provided data (from folder “test_data/”) is retrievable via the following Url: http://127.0.0.1:8000/heatmap_file_comparison_d3.html

![ File comparison heatmap](/images/img_heat1.png)


## Technical details

The following class diagram shows the structure of the prototype:

![Class diagram of the prototype](/images/classdiagram.png)
