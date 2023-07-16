
library("shiny")
library("dplyr")
library("ggplot2")
library("countrycode")
library("plotly")

# Teodor Chakarov - 12141198

# Reading the Data
load(file="./data_cia.rda")

map <- map_data("world")
map$iso2c <- countrycode::countrycode(sourcevar = map$region,
                                            origin = "country.name",
                                            destination = "iso2c", nomatch = NA)
# Data with appended coordinates for locations
joint_data<-left_join(map,data_cia, by = c("iso2c" = "iso2c"))

ui <- fluidPage(

    titlePanel("CIA World Factbook 2020"),
    h3("Author: Teodor Chakarov"),
    # Adding a Panel for the input fields and buttons
    sidebarPanel(

            selectInput("ComboCols", "Choose a variable", choices = c("Median Age","Youth Unemployment Rate")),
            actionButton("click", "View Raw Data"),
            dataTableOutput("TableOutput")

    ),
    # For the plot
    mainPanel(

            titlePanel("Map Vizualization"),
            plotlyOutput("plot", width = "1000px", height = "800px")
    )

)

server <- function(input, output, session) {
   
  output$plot <- renderPlotly({
    # Selects one depending on the selected input
    if(input$ComboCols=="Median Age"){
                            p<-ggplot(joint_data, aes(x = long, y = lat, group = group, text = paste('Country: ', country, '<br>Median Age: ', median_age), fill=median_age)) +
                              scale_fill_viridis_c("Median Age")+
                              geom_polygon(color="white")
                              ggplotly(p, tooltip = c("text"))
    }else{
                            p<-ggplot(joint_data, aes(x = long, y = lat, group = group, text = paste('Country: ', country, '<br>Youth unempl.Rate ', youth_unempl_rate ), fill=youth_unempl_rate)) +
                              scale_fill_viridis_c("Youth Unempl. Rate")+
                              geom_polygon(color="white")
                              ggplotly(p, tooltip = c("text"), width = 1070)
  }
    
  })
  # renaming the column names
  NewColNamesData<-data_cia%>%
    rename(
      Country=country,
      'Median Age' = median_age,
      'Youth Unempl. Rate' = youth_unempl_rate,
      'ISO 2' = iso2c,
      'ISO 3' = iso3c,
      'Developed/ing Countries' = Developed...Developing.Countries,
      'Region Name' = Region.Name,
      'Sub region Name' = Sub.region.Name,
      'ISO 3166' = ISO3166.1.Alpha.3
    )
  # On Button Click open Table
  observeEvent(input$click, {
    output$TableOutput <- renderDataTable(NewColNamesData, options = list(scrollX=TRUE, pageLength = 15))
  })
  
}
shinyApp(ui, server)



















































