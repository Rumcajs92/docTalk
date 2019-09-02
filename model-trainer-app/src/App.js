import React from "react";

import Recorder from "recorder-js";
import "./App.css";
import Button from "react-bootstrap/Button";
import ButtonGroup from "react-bootstrap/ButtonGroup";
import Container from "react-bootstrap/Container";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";
import Helmet from "react-helmet/es/Helmet";
import AudioRecorder from "react-audio-recorder";
import { ReactMic } from "react-mic";
import ReactPlayer from "react-player";

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      record: false,
      blob: null,
      play: false
    };
    this.onStop = this.onStop.bind(this);
  }

  startRecording = () => {
    this.setState({
      record: true
    });
  };

  stopRecording = () => {
    this.setState({
      record: false
    });
  };

  onData(recordedBlob) {
    console.log("chunk of real-time data is: ", recordedBlob);
  }

  onStop(recordedBlob) {
    console.log("recordedBlob is: ", recordedBlob);
    this.setState({
      blob: recordedBlob
    });
  }

  render() {
    const config = {
      // attributes: {
      //     controls: true
      // },
      file: {
        forceAudio: true
      }
    };
    return (
      <div>
        <Helmet>
          <meta charSet="utf-8" />
          <title>My Title</title>
          <link
            rel="stylesheet"
            href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
            integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
            crossOrigin="anonymous"
          />
        </Helmet>
        <Container className="border">
          <Row>
            <Col className="border">
              <ReactMic
                record={this.state.record}
                visualSetting="frequencyBars"
                onStop={this.onStop}
                onData={this.onData}
                strokeColor="#000000"
                backgroundColor="#FF4081"
              />
            </Col>
          </Row>
          <Row>
            <Col className="border">
              <button onClick={this.startRecording} type="button">
                Record
              </button>
            </Col>
            <Col className="border">
              <button onClick={this.stopRecording} type="button">
                Stop
              </button>
            </Col>
          </Row>
          {this.state.record && (
            <Row>
              <Col className="border">"Recording..."</Col>
            </Row>
          )}
          {this.state.blob && (
            <Row>
              <Container className="border">
                <Row>
                  <Col className="border">
                    <ReactPlayer
                      url={this.state.blob.blobURL}
                      playing={this.state.play}
                      onPlay={this.logStart}
                      controls={true}
                      width={"100%"}
                      height={"100%"}
                      config={config}
                    />
                  </Col>
                </Row>
              </Container>
            </Row>
          )}
        </Container>
      </div>
    );
  }

  logStart = () => {
    console.log("playing");
  };
}

export default App;
