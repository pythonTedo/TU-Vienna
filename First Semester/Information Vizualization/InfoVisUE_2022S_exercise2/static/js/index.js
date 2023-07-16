
var margin = {top: 10, right: 30, bottom: 30, left: 60},
    width = 460 - margin.left - margin.right,
    height = 400 - margin.top - margin.bottom;

const svg = d3.select("#svg_scatter")
    .append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform", `translate(${margin.left}, ${margin.top})`);

d3.csv('static/data/pca.csv').then(function(data) {

  const x = d3.scaleLinear()
        .domain([-1.5, 2])
        .range([ 0, width ]);
    svg.append("g")
        .attr("transform", `translate(0, ${height})`)
        .call(d3.axisBottom(x));

    // Add Y axis
  const y = d3.scaleLinear()
        .domain([-1,1])
        .range([ height, 0]);
    svg.append("g")
        .call(d3.axisLeft(y));

    var color = d3.scaleOrdinal(d3.schemeCategory10);

    const mouseover= function(d) {
        d3.selectAll("circle.pt" + d.country)
        .style("stroke", "black")
        .style("opacity", 1)
        d3.selectAll("rect.pt" + d.country)
        .style("stroke", "black")
        .style("opacity", 1)
    }

    // on moving mouse to cirlce
    const mousemove = function(event,d) {
        d3.selectAll("circle.pt" + d.country)
          .attr("r", 6)
          .style("stroke", "black")
          .style("opacity", 1)
    }

    const mouseleave = function(d) {
              d3.selectAll("circle.pt" + d.country)
              .style("stroke", "none")
              .style("opacity", 0.8)
              d3.selectAll("rect.pt" + d.country)
              .style("stroke", "none")
              .style("opacity", 0.8)       
            }
        
    svg.selectAll("dot")
        .data(data)
        .join("circle")
        .attr("cx", function (d) { return x(d.PCA_Component_One); } )
        .attr("cy", function (d) { return y(d.PCA_Component_Two); } )
        .attr("r", 4)
        .attr("fill", function(d){ return color(d.country)})
        .attr("class", function(d) { return "pt" + d.country; })
        .on("mouseover", mouseover)
        .on("mousemove", mousemove)
        .on("mouseleave", mouseleave)    
});


///////////////// HEATMAP ///////////////////////////

const margin1 = {top: 30, right: 1, bottom: 90, left: 250},
    width1 = 800 - margin1.left - margin1.right,
    height1 = 500 - margin1.top - margin1.bottom;

    const svg1 = d3.select("#svg_heatmap")
    .append("svg")
    .attr("width", width1 + margin1.left + margin1.right)
    .attr("height", height1 + margin1.top + margin1.bottom)
    .append("g")
    .attr("transform", `translate(${margin1.left}, ${margin1.top})`);

    d3.csv("/static/data/melted.csv").then(function(data) {

    // Labels of row and columns -> unique identifier of the column called 'group' and 'variable'
    const Country = Array.from(new Set(data.map(d => d.Countries)))
    const Category = Array.from(new Set(data.map(d => d.variable)))
    
    // Build X scales and axis:
    const x = d3.scaleBand()
        .range([ 0, width1 ])
        .domain(Country)
        .padding(0.05);
      svg1.append("g")
        .style("font-size", 10)
        .attr("transform", `translate(0, ${height1})`)
        .call(d3.axisBottom(x).tickSize(0))
        .selectAll("text")
        .attr("transform", "translate(-10,10)rotate(-45)")
        .style("text-anchor", "end")
        .select(".domain").remove()
    

    // Build Y scales and axis:
    const y = d3.scaleBand()
        .range([ height1, 0 ])
        .domain(Category)
        .padding(0.05);
      svg1.append("g")
        .style("font-size", 10)
        .call(d3.axisLeft(y).tickSize(0))
        .select(".domain").remove()


    const myColor = d3.scaleSequential()
      .interpolator(d3.interpolatePlasma)
      .domain([1,100])
    

    // create a tooltip - with the info on mouse hover
    const tooltip = d3.select("#svg_heatmap")
    .append("div")
    .style("opacity", 0)
    .attr("class", "tooltip")
    .attr("ycls", "tooltip")
    .style("background-color", "pink")
    .style("border", "solid")
    .style("border-width", "2px")
    .style("border-radius", "5px")
    .style("padding", "5px")

    // Three function that change the tooltip when user hover / move / leave a cell
    const mouseover1 = function(d) {
        tooltip
          .html(d.Countries + "<br>Value: "+ d.value)
          .style('left', d3.event.pageX + 30 + 'px')
          .style('top', d3.event.pageY - 28 + 'px')
          .transition()
          .style('opacity', 1);

        d3.selectAll("rect.pt" + d.Countries)
        .style("stroke", "black") 
        .style("opacity", 1)
        d3.selectAll("circle.pt" + d.Countries)
        .style("stroke", "black")
        .style("opacity", 1)
     }

     const mousemove1 = function(event,d) {
        // tooltip
        // .html("The exact value of<br>this cell is: " + d.value)
        // .style("left", (event.x)/2 + "px")
        // .style("top", (event.y)/2 + "px")
          
      }

      // heatmap
      const mouseleave1 = function(d) {
        tooltip
          .style("opacity", 0) 
          d3.selectAll("rect.pt" + d.Countries)
          .style("stroke", "none")
          .style("opacity", 0.8)
          d3.selectAll(this)
          .style("stroke", "none")
          .style("opacity", 0.8)
          d3.selectAll("circle.pt" + d.Countries)
          .style("stroke", "none")
          .style("opacity", 0.8)
      }
     svg1.selectAll()
     .data(data, function(d) {return d.Countries+':'+d.variable;})
     .join("rect")
       .attr("x", function(d) { return x(d.Countries) })
       .attr("y", function(d) { return y(d.variable) })
       .attr("rx", 4)
       .attr("ry", 4)
       .attr("width", x.bandwidth() )
       .attr("height", y.bandwidth() )
       .style("fill", function(d) { return myColor(d.value)} )
       .attr("class", function(d) { return "pt" + d.Countries})
       .attr("ycls", function(d) { return "pt" + d.variable; })
       .style("stroke-width", 4)
       .style("stroke", "none")
       .style("opacity", 0.8)
     .on("mouseover", mouseover1)
     .on("mousemove", mousemove1)
     .on("mouseleave", mouseleave1)
   });


