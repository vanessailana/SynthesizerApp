/*
 * Copyright 2011 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.csun_comp380_15884.group3.synthesizerapp.midi;

/**
 * An interface for listening for any kind of midi event.
 *
 */
public interface MidiListener {
  // Control events.
  void onNoteOff(int channel, int note, int velocity);
  void onNoteOn(int channel, int note, int velocity);
}
