# UI Automation Framework: Java Playwright & Allure Report

This repository contains a professional-grade automated testing framework designed to ensure the quality and functionality of a web application. It utilizes modern testing tools to simulate user behavior, identify defects, and provide detailed visual reporting.

---

## Project Overview

The primary goal of this project is to automate end-to-end user scenarios. Built with **Java** and **Playwright**, the framework follows industry best practices to ensure the code is maintainable, scalable, and easy to understand for both technical and non-technical stakeholders.

### Key Technologies

* **Java**: The primary programming language for robust logic.
* **Playwright**: A modern automation library for fast and reliable cross-browser testing.
* **Allure Report**: A flexible reporting tool that provides a visual representation of test results.
* **GitHub Actions**: A Continuous Integration (CI) tool that runs tests automatically on every code update.

---

## Architecture: Page Object Model (POM)

This framework is organized using the **Page Object Model**. In this design, each page of the website is represented by a specific Java class. This separation ensures that if the website's design changes, only the affected page class needs an update, keeping the test logic intact.

The suite consists of **26 test cases** organized into **7 functional areas**:

| Page Class | Responsibility |
| --- | --- |
| **Homepage** | Validates navigation, scrolling behavior, and subscription features. |
| **Login Page** | Manages user authentication, including registration, login, and logout. |
| **Products Page** | Handles product searching, category filtering, and product reviews. |
| **Cart Page** | Ensures items can be added, removed, and verified for correct quantities. |
| **Checkout Page** | Validates the purchasing flow, address verification, and invoice downloads. |
| **Contact Page** | Tests the functionality of the "Contact Us" form. |
| **Test Cases Page** | Verifies the accessibility of the documentation page. |

---

## Test Execution Groups

To optimize testing time, tests are categorized into two execution groups:

* **Smoke Test Suite**: A high-priority group covering the **Cart, Checkout, and Products** pages. This ensures the core "path to purchase" is functional before deeper testing begins.
* **Regression Test Suite**: A comprehensive execution of all 26 test cases to ensure new code changes haven't broken existing functionality.

---

## Automated Workflow and Reporting

This project is fully integrated with a CI/CD pipeline via **GitHub Actions**. This automation removes the need for manual intervention by performing the following:

1. **Trigger**: Tests run automatically on every code "push" or can be triggered manually by a user.
2. **Execution**: The pipeline sets up a virtual environment and runs the Java Playwright suite.
3. **Report Generation**: Results are processed into an **Allure Report**, which includes pass/fail status and execution time.
4. **Deployment**: The final report is automatically hosted on **GitHub Pages**, providing a permanent URL for stakeholders to review the latest test outcomes.
5. https://woozie15273.github.io/AllureReportExercise/
