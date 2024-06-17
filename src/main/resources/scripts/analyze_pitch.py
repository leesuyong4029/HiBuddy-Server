import parselmouth
import sys

def analyze_pitch(audio_file_path):
    sound = parselmouth.Sound(audio_file_path)
    pitch = sound.to_pitch()
    mean_pitch = pitch.get_mean()
    return mean_pitch

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python analyze_pitch.py <audio_file_path>")
        sys.exit(1)
    audio_file_path = sys.argv[1]
    mean_pitch = analyze_pitch(audio_file_path)
    print(mean_pitch)
