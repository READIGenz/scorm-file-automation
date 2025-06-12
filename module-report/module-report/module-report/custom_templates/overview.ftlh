<!DOCTYPE html>
<html>
<head xmlns="http://www.w3.org/1999/xhtml">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style type="text/css">

        html, body, h1, h2, h3, h4, h5, h6 {
            background: #f7f8f3; /* Old browsers */
            font-family: "Arial", sans-serif;
        }

        h1 {
            margin-top: 50px;
            color: white;
            padding-left: 10px;
            background: #74bbd9; /* Old browsers */
            background: -moz-linear-gradient(top,  #00915A 0%, #00A071 50%, #00AC7D 35%, #00915A 100%); /* FF3.6-15 */
            background: -webkit-linear-gradient(top,  #00915A 0%, #00A071 50%, #00AC7D 35%, #00915A 100%); /* Chrome10-25,Safari5.1-6 */
            background: linear-gradient(to bottom,  #00915A 0%, #00A071 50%, #00AC7D 35%, #00915A 100%); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#74bbd9', endColorstr='#44a4cb', GradientType=0); /* IE6-9 */
            font-weight: normal;
            font-size: larger;
            height: 50px;
            line-height: 50px;

        }

        h2 {
            color: white;
            padding-left: 10px;
            background: #49c0f0; /* Old browsers */
            background: -moz-linear-gradient(top, #00AC7D 50%, #00A071 100%); /* FF3.6-15 */
            background: -webkit-linear-gradient(top, #00AC7D 50%, #00A071 100%); /* Chrome10-25,Safari5.1-6 */
            background: linear-gradient(to bottom, #00AC7D 50%, #00A071 100%); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#49c0f0', endColorstr='#2cafe3', GradientType=0); /* IE6-9 */
            font-size: large;
            height: 30px;
            line-height: 30px;
            font-weight: normal;
            margin-top: 20px;
            margin-bottom: 2px;

        }

        h3 {
            background-color: #f1f4e7
        }

        th {
            border: 2px solid #cddfe4;
            background: #74bbd9; /* Old browsers */
            background: -moz-linear-gradient(top, #00915A 0%, #00A071 50%, #00AC7D 35%, #00915A 100%); /* FF3.6-15 */
            background: -webkit-linear-gradient(top, #00915A 0%, #00A071 50%, #00AC7D 35%, #00915A 100%); /* Chrome10-25,Safari5.1-6 */
            background: linear-gradient(to bottom,  #00915A 0%, #00A071 50%, #00AC7D 35%, #00915A 100%); /* W3C, IE10+, FF16+, Chrome26+, Opera12+, Safari7+ */
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#74bbd9', endColorstr='#44a4cb', GradientType=0); /* IE6-9 */
            color: white;
            height: 30px;
            line-height: 30px;
            font-weight: normal;
        }

        td {
            border: 1px solid #cddfe4;
            padding: 5px;
        }

        table {
            border: 1px solid #cddfe4;
            border-collapse: collapse;
            width: 100%;
        }

        .chart {
            border: 1px solid #cddfe4;
            border-collapse: collapse;
            background-color: none;
        }

        .passed {
            background-color: #f4f8ec;
            font-weight: normal;
            color: black;
            border: 1px solid green;
        }

        .skipped {
            background-color: silver;
            font-weight: bold;
            color: darkgray
        }

        .failed {
            background-color: white;
            font-weight: normal;
            color: red;
            border: 1px solidred;

        }

        .undefined {
            background-color: white;
            font-weight: bold;
            color: goldenrod
        }

        .lowest {
            background-color: lightyellow;
            color: black
        }

        .low {
            background-color: gold;
            color: black
        }

        .medium {
            background-color: orange;
            color: black
        }

        .high {
            background-color: orangered;
            color: black
        }

        .highest {
            background-color: darkred;
            color: black
        }

        .passed_description {
            background-color: #D0FFD0;
            font-weight: bold;
            font-style: italic;
            color: black
        }

        .failed_description {
            background-color: #FFA898;
            font-weight: bold;
            font-style: italic;
            color: black
        }

        .noborder {
            border: none;
            border-collapse: collapse;
            vertical-align: top;
        }

        OL {
            counter-reset: item
        }

        OL > LI {
            display: block
        }

        OL > LI:before {
            content: counters(item, ".") " ";
            counter-increment: item
        }

        div.header {
            display: block;
            text-align: center;
            position: running(header);
        }

        div.footer {
            display: block;
            text-align: center;
            position: running(footer);
        }

        div.content {
            page-break-after: always;
        }

        div.content:last-child {
            page-break-after: avoid;
        }

        span a {
            color: black;
        }

        @page {
            size: A4 landscape;
            @top-center {
                content: "Overall Results";
                color: silver;
                font-size: 14px;
            }
            @top-right {
                content: date("dd MMM, yyyy hh:mm");
                color: silver;
                font-size: 8px;
            }
            @bottom-right {
                content: "Page " counter(page) " of " counter(pages) ;
                color: silver;
                font-size: 8px;
            }
        }

        ol a::after {
            content: leader('.') target-counter(attr(href), page);
        }
    </style>

    <title>${title}</title>
    <meta charset="UTF-8"/>
</head>
<body>
<div class='header'></div>
<div class='footer'></div>
<#include "pie_chart">
<#include "tables">
<h1>Overview Chart</h1>
<table class="chart">
    <tr>
        <th class="chart">Features Status</th>
        <th class="chart">Scenario Status</th>
    </tr>
    <tr class="chart">
        <td class="chart">
            <@drawPieChart
            id="feature_status"
            width=450 height=300
            values=[overallStats.getFeaturesPassed(),
            overallStats.getFeaturesFailed(),
            overallStats.getFeaturesUndefined(),
            overallStats.getFeaturesKnown()]
            labels=["Passed", "Failed", "Undefined", "Known"]
            colors=["#a4c439", "#f1072b", "silver", "gold"]
            shadowColors=["darkgrey", "darkgrey", "darkgrey", "darkgrey"]
            pieThickness=15
            shift=2 />
        </td>
        <td class="chart">
            <@drawPieChart
            id="scenario_status"
            width=450 height=300
            values=[overallStats.getScenariosPassed(),
            overallStats.getScenariosFailed(),
            overallStats.getScenariosUndefined(),
            overallStats.getScenariosKnown()]
            labels=["Passed", "Failed", "Undefined", "Known"]
            colors=["#a4c439", "#f1072b", "silver", "gold"]
            shadowColors=["darkgrey", "darkgrey", "darkgrey", "darkgrey"]
            pieThickness=15
            shift=2 />
        </td>
    </tr>
</table>

<h1>Summary</h1>

<@drawOverviewStatsTable stats=overallStats />

<h1>Features Status</h1>

<table>
    <tr>
        <th>Feature Name</th>
        <th>Status</th>
        <th>Passed</th>
        <th>Failed</th>
        <th>Known</th>
        <th>Undefined</th>
        <th>Total</th>
        <th>Duration</th>
    </tr>
    <#list features as feature>
        <#if feature.featureName?has_content>
            <tr class="${feature.status}">
                <td>${feature.featureName}</td>
                <td>${feature.status}</td>
                <td>${feature.stats.scenariosPassed}</td>
                <td>${feature.stats.scenariosFailed}</td>
                <td>${feature.stats.scenariosKnown}</td>
                <td>${feature.stats.scenariosUndefined}</td>
                <td>${feature.stats.getScenariosTotal()}</td>
                <td>${feature.duration}s</td>
            </tr>
        </#if>
    </#list>

</table>

<h1>Scenario Status</h1>

<table>
    <tr>
        <th>Feature Name</th>
        <th>Scenario</th>
        <th>Status</th>
        <th>Passed</th>
        <th>Failed</th>
        <th>Known</th>
        <th>Undefined</th>
        <th>Total</th>
        <th>Retries</th>
        <th>Duration</th>
    </tr>
    <#list scenarios as scenario>
        <tr class="${scenario.status}">
            <td>${scenario.featureName}</td>
            <td>${scenario.scenarioName}</td>
            <td>${scenario.status}</td>
            <td>${scenario.stats.stepsPassed}</td>
            <td>${scenario.stats.stepsFailed}</td>
            <td>${scenario.stats.stepsKnown}</td>
            <td>${scenario.stats.stepsUndefined}</td>
            <td>${scenario.stats.getStepsTotal()}</td>
            <td>${scenario.retries}</td>
            <td>${scenario.duration}s</td>
        </tr>
    </#list>
</table>
</body>
</html>