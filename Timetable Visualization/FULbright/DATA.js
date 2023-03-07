
//I'm having trouble insert cái data này automatic vào cái file .js t đang chạy.
// Toàn phải làm bằng tay.
// giúp t với dc không


// classroom format
var classroomData = [{
      text: "CR 1",
      id: "CR_1",
      capacity: 30
  }, {
      text: "CR 2",
      id: "CR_1",
      capacity: 30
  }, {
      text: "CR 3",
      id: "CR_1",
      capacity: 30
  }, {
      text: "CR 4",
      id: "CR_1",
      capacity: 30
  }, {
      text: "CR 5",
      id: "CR_1",
      capacity: 30
  }, {
      text: "CR 6",
      id: "CR_1"
  }, {
      text: "CR 7",
      id: "CR_1",
      capacity: 30
  }, {
      text: "CR 8",
      id: "CR_1",
      capacity: 30
  }
];

//course format

var courseData = [{
    id: "ART101",
    text: "Introduction to Visual Studies",
    instructor: "Tram Luong",
    capacity: "50",
    duration: 90,
    color: "#cb6bb2"
  }, {
    id: "CORE202",
    text: "Global Humanities and Social Change",
    instructor: "Mathew McDonald",
    duration: 60,
    color: "#56ca85",
    capacity: "50",
  }, {
    id: "CORE303",
    text: "Design and Systems Thinking",
    instructor: "	Truong Trung Kien",
    duration: 90,
    color: "#1e90ff",
    capacity: "50"
  }, {
    id: "CS101",
    text: "Computer Science I",
    instructor: "Phan Thanh Trung",
    duration: 90,
    color: "#ff9747",
    capacity: "50"
  }, {
    id: "ECON202",
    text: "Behavioural and Experimental Economics",
    instructor: "Graeme Walker",
    duration: 60,
    color: "#f05797",
    capacity: "50"
  }, {
    id:"ECON302",
    text: "Economics of Inequality",
    instructor: "Khieu Van Hoang",
    duration: 90,
    color: "#2a9010",
    capacity: "50"
  }];

// xếp lịch format
// tại vì cái library, t đéo biết làm sao để nó không phải là specific date, nên là tất cả ngầm hiểu là từ ngày 13/2/2023 đến ngày 19/2/2023 nha
  var data = [{
    classroomId: "CR_1",
    courseId: "ECON303",
    capacity: 30,
    startDate: new Date(2023, 2, 15, 9, 10), //format thời gian: YYYY,MM,dd,hh,mm  
    endDate: new Date(2023, 2, 15, 11, 1)    // cái này có thêm end time, có cách nào m tính luôn thời gian bắt đầu và thời gian kết thúc cho t dc k?
},

]