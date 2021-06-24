// app.js
App({
  onLaunch() {
    wx.connectSocket({
      url: 'ws://localhost:18092/test/oneToOne/2'
    })
    wx.onSocketClose(function (res) {
      wx.connectSocket({
        url: 'ws://localhost:18092/test/oneToOne/2'
      })
    })
  },

 
  globalData: {
    userInfo: null
  }
})
